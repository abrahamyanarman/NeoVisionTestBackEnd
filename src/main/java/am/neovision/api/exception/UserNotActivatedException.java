package am.neovision.api.exception;

public class UserNotActivatedException extends Exception {
    public UserNotActivatedException(){
        super("Your account is not activate yet, please confirm your email");
    }
}
