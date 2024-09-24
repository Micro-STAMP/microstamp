package microstamp.authorization.exception;

public class DistinctPasswordException extends RuntimeException {

    public DistinctPasswordException() {
        super("Passwords don't match");
    }

}