package microstamp.step2.exception;

public class Step2EnvironmentResponsibilityException extends RuntimeException {

    public Step2EnvironmentResponsibilityException() {
        super("A responsibility cannot be assigned to Environment");
    }

}
