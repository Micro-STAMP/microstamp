package microstamp.step2.exception;

public class Step2EnvironmentParentException extends RuntimeException {

    public Step2EnvironmentParentException() {
        super("Environment cannot be a father");
    }

}
