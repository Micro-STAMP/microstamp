package microstamp.step2.exception;

public class Step2SelfParentingComponentException extends RuntimeException {

    public Step2SelfParentingComponentException() {
        super("A Component can't have itself as its father");
    }

}
