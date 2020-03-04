package am.neovision.api.controller;

import am.neovision.api.dto.Response;
import am.neovision.api.dto.SignUpRequestDto;
import am.neovision.api.exception.AccountExistsException;
import am.neovision.api.exception.UserNotFoundException;
import am.neovision.api.service.EmailService;
import am.neovision.api.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/register")
public class RegisterController {

    private UserService userService;

    private EmailService emailService;


    @Value("${front.url}")
    private String frontURL;

    public RegisterController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @PostMapping(value = "/user")
    public ResponseEntity<Response> addUser(@RequestBody @NonNull SignUpRequestDto userRequest) throws AccountExistsException {
        userService.addUser(userRequest);
        Response response = sendMessage(userRequest.getEmail());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping(value = "/confirm/{code}")
    public ResponseEntity<Response> confirm(@PathVariable String code) throws UserNotFoundException {
        userService.confirmUser(code);
        Response response = new Response("your account has been activated");
        response.setSuccess(true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private Response sendMessage(@NonNull String email) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("Complete Registration!");
        simpleMailMessage.setText("To confirm your account, please click here :   "
                + frontURL + "register/confirm/" + userService.generateCode(email));
        emailService.sendEmail(simpleMailMessage);
        Response response = new Response("confirm your mail");
        response.setSuccess(true);
        return response;
    }
}
