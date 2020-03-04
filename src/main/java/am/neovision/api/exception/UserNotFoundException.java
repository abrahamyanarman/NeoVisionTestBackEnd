package am.neovision.api.exception;

public class UserNotFoundException extends Exception {


    public UserNotFoundException(String msg){
        super("user with name " + msg + " not found");
    }
    public UserNotFoundException(long id){
        super("user not found");
    }
}
