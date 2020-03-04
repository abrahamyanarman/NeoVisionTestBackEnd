package am.neovision.api.controller;


import am.neovision.api.dto.Response;
import am.neovision.api.exception.UserNotFoundException;
import am.neovision.api.service.EmailService;
import am.neovision.api.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/reset")
@CrossOrigin
public class ResetPasswordController {

    private UserService userService;

    private EmailService emailService;

    @Value("${front.url}")
    private String frontURL;

    public ResetPasswordController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping(value = "/user/{email}")
    public ResponseEntity<Response> resetUserEmail(@PathVariable @NonNull String email) throws Exception {
        userService.checkEmail(email);
        long code = userService.generateCode(email);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("Reset Password!");
        simpleMailMessage.setText("To change your account password, please click here :   "
                + frontURL + "reset-password/" + code);
        emailService.sendEmail(simpleMailMessage);
        Response response = new Response("check your mail");
        response.setSuccess(true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping(value = "/user/{emailCode}")
    public ResponseEntity<Response> changeUserPassword(@PathVariable @NonNull String emailCode, @RequestParam @NonNull String password) throws UserNotFoundException {
        userService.changePassword(emailCode, password);
        Response response = new Response("password has been successfully changed");
        response.setSuccess(true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
