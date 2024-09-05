package microstamp.step2.enumeration;

import java.util.Arrays;
import java.util.List;

public enum ConnectionActionType {

    CONTROL_ACTION,
    FEEDBACK,
    COMMUNICATION_CHANNEL,
    PROCESS_INPUT,
    PROCESS_OUTPUT,
    DISTURBANCE;

    public static List<ConnectionActionType> getDefaultTypes() {
        return Arrays.asList(
                CONTROL_ACTION,
                FEEDBACK,
                COMMUNICATION_CHANNEL
        );
    }

    public static List<ConnectionActionType> getSourceEnvironmentTypes() {
        return Arrays.asList(
                PROCESS_INPUT,
                DISTURBANCE
        );
    }

    public static List<ConnectionActionType> getTargetEnvironmentTypes() {
        return List.of(PROCESS_OUTPUT);
    }
}
