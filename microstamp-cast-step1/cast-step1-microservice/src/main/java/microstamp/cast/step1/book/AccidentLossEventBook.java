package microstamp.cast.step1.book;

public class AccidentLossEventBook {

    public static final String DELETE_HAZARD_ASSOCIATION =
            "DELETE FROM cast_hazard_accident_loss_event WHERE accident_loss_event_id = ?1";

}
