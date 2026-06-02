package microstamp.cast.step1.book;

public class CastHazardBook {

    public static final String DELETE_ACCIDENT_LOSS_EVENT_ASSOCIATION =
            "DELETE FROM cast_hazard_accident_loss_event WHERE hazard_id = ?1";

    public static final String DELETE_VIOLATED_CONSTRAINT_ASSOCIATION =
            "DELETE FROM cast_violated_constraint_hazard WHERE hazard_id = ?1";

}
