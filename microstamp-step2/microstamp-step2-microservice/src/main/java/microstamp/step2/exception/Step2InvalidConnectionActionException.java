package microstamp.step2.exception;

public class Step2InvalidConnectionActionException extends RuntimeException {

    public Step2InvalidConnectionActionException() {
        super("Invalid connection");
    }

    public Step2InvalidConnectionActionException(final String message) {
        super(message);
    }

}
