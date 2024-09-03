package step3.infra.exceptions;

public class OperationNotAllowedException extends RuntimeException{
    public OperationNotAllowedException(String message) {
        super(message);
    }
}
