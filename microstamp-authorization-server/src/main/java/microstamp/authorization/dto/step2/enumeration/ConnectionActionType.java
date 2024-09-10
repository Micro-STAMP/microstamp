package microstamp.authorization.dto.step2.enumeration;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum ConnectionActionType {

    CONTROL_ACTION("ControlAction"),
    FEEDBACK("Feedback"),
    COMMUNICATION_CHANNEL("CommunicationChannel"),
    PROCESS_INPUT("ProcessInput"),
    PROCESS_OUTPUT("ProcessOutput"),
    DISTURBANCE("Disturbance");

    private final String formattedName;

    ConnectionActionType(String formattedName) {
        this.formattedName = formattedName;
    }

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
