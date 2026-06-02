package microstamp.cast.step1.book;

public class ViolatedSystemSafetyConstraintBook {

    public static final String DELETE_HAZARD_ASSOCIATION =
            "DELETE FROM cast_violated_constraint_hazard WHERE violated_constraint_id = ?1";

}
