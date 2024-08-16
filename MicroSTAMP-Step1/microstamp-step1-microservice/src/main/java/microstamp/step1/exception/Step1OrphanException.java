package microstamp.step1.exception;

public class Step1OrphanException extends RuntimeException {

    public Step1OrphanException() {
        super("A Hazard can't be child of his child");
    }

}
