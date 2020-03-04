package am.neovision.api.controller;

import am.neovision.api.dto.Response;
import am.neovision.api.dto.UserInfoRequest;
import am.neovision.api.dto.UserInfoResponse;
import am.neovision.api.exception.UserNotFoundException;
import am.neovision.api.mapper.UserMapper;
import am.neovision.api.model.User;
import am.neovision.api.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/info")
    public ResponseEntity<UserInfoResponse> getUserInfo(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        UserMapper userMapper = new UserMapper();
        UserInfoResponse userInfoResponse = userMapper.apply(user);
        return new ResponseEntity<>(userInfoResponse, HttpStatus.OK);
    }
    @GetMapping(value = "/info/{id}")
    public ResponseEntity<UserInfoResponse> getUserInfoById(@PathVariable Long id) {
        User user =  userService.findById(id);
        UserMapper userMapper = new UserMapper();
        UserInfoResponse userInfoResponse = userMapper.apply(user);
        return new ResponseEntity<>(userInfoResponse, HttpStatus.OK);
    }

    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN')")
    @DeleteMapping(value = "/{d}")
    public ResponseEntity<Response> deleteUser(@PathVariable @NonNull long id) throws UserNotFoundException {
        userService.deleteUser(id);
        Response response = new Response("user Successfully deleted");
        response.setSuccess(true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN')")
    @RequestMapping(value = "/allusers", method = RequestMethod.GET)
    public ResponseEntity<List<UserInfoResponse>> getAll() {
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Response> activateOrDeactivateUser(@PathVariable @NonNull long id,@RequestParam @NonNull boolean activationStatus) throws UserNotFoundException {
        userService.activateOrDeactivateUser(id,activationStatus);
        Response response = new Response("User activation status Successfully updated");
        response.setSuccess(true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize(value = "hasAuthority('ROLE_CLIENT')")
    @PutMapping(value = "/updateUser")
    public ResponseEntity<Response> updateUserInfoByUser(@RequestBody @NonNull UserInfoRequest userInfoRequest, Authentication authentication){
        userService.updateUserInfoByUser(userInfoRequest,authentication);
        Response response = new Response("User Successfully updated");
        response.setSuccess(true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN')")
    @PostMapping(value = "/updateUser/{id}")
    public ResponseEntity<Response> updateUserInfoByAdmin(@RequestBody @NonNull UserInfoRequest userInfoRequest,@PathVariable Long id){
        userService.updateUserInfoByAdmin(userInfoRequest,id);
        Response response = new Response("User Successfully updated");
        response.setSuccess(true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
