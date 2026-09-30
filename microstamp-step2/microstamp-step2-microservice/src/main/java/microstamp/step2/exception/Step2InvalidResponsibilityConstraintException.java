package microstamp.step2.exception;

public class Step2InvalidResponsibilityConstraintException extends RuntimeException {

    public Step2InvalidResponsibilityConstraintException() {
        super("A responsibility must be linked to exactly one constraint: either a systemSafetyConstraintId (STPA) or a violatedSystemSafetyConstraintId (CAST), not both or neither");
    }

}
