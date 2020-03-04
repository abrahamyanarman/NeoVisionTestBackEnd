package am.neovision.api.controller;


import am.neovision.api.dto.ErrorResponse;
import am.neovision.api.dto.SignInRequest;
import am.neovision.api.dto.UserInfoResponse;
import am.neovision.api.mapper.UserMapper;
import am.neovision.api.model.User;
import am.neovision.api.security.JwtTokenUtil;
import am.neovision.api.service.impl.MyUserDetailsServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController()
@RequestMapping(value = "/api/auth")
public class LoginController {

    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;
    private MyUserDetailsServiceImpl userDetailsService;

    public LoginController(JwtTokenUtil jwtTokenUtil,
                           MyUserDetailsServiceImpl userDetailsService, AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }


    @PostMapping(value = "/signin")
    public ResponseEntity<?> login(@RequestBody @NonNull SignInRequest request) {
        final User userDetails;
        final UserInfoResponse userResponse;

        try {
            authenticate(request.getEmail(), request.getPassword());

            userDetails = (User) userDetailsService
                    .loadUserByUsername(request.getEmail());
        } catch (Exception e) {
            ErrorResponse response = new ErrorResponse(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        final String token = jwtTokenUtil.generateToken(userDetails);
        UserMapper userMapper = new UserMapper();
        userResponse = userMapper.apply(userDetails);
        userResponse.setToken(token);

        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }


    private void authenticate(String userEmail, String password) throws Exception {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userEmail, password));
        } catch (DisabledException e) {
            throw new Exception("User Disabled" +
                    "", e);
        } catch (BadCredentialsException e) {
            throw new Exception("Bad Credentials", e);
        }
    }
}
