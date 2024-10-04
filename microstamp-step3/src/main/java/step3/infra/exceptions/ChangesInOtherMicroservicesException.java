package step3.infra.exceptions;

public class ChangesInOtherMicroservicesException extends RuntimeException{
    public ChangesInOtherMicroservicesException(String message) {
        super(message);
    }
}
