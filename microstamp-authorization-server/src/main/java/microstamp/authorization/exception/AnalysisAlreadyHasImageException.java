package microstamp.authorization.exception;

public class AnalysisAlreadyHasImageException extends RuntimeException {

    public AnalysisAlreadyHasImageException() {
        super("This analysis already has an associated image");
    }

}