package microstamp.step1.exception;

public class Step1SelfParentingHazardException extends RuntimeException {

    public Step1SelfParentingHazardException() {
        super("A Hazard can't have itself as its father");
    }

}
