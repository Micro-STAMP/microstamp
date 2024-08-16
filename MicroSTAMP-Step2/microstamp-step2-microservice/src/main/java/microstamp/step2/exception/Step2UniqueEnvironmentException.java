package microstamp.step2.exception;

public class Step2UniqueEnvironmentException extends RuntimeException {

    public Step2UniqueEnvironmentException() {
        super("A Control Structure from an Analysis can only have one Component of type 'Environment'");
    }

}
