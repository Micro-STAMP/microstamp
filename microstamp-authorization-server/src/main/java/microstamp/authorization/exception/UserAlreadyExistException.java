package microstamp.authorization.exception;

public class UserAlreadyExistException extends RuntimeException {

    public UserAlreadyExistException() {
        super("An account with that username already exists.");
    }

}
