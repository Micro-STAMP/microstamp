package microstamp.step2.exception;

public class Step2InvalidInteractionException extends RuntimeException {

    public Step2InvalidInteractionException() {
        super("Invalid connection");
    }

    public Step2InvalidInteractionException(final String message) {
        super(message);
    }

}
