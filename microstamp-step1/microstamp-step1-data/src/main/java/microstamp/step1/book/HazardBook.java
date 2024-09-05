package microstamp.step1.book;

public class HazardBook {

    public static final String FIND_HAZARD_CHILDREN = "SELECT * FROM hazards WHERE father_id = ?1";

    public static final String DELETE_LOSSES_ASSOCIATION = "DELETE FROM hazard_loss WHERE hazard_id = ?1";

    public static final String DELETE_SYSTEM_SAFETY_CONSTRAINT_ASSOCIATION = "DELETE FROM system_safety_constraint_hazard WHERE hazard_id = ?1";

}
