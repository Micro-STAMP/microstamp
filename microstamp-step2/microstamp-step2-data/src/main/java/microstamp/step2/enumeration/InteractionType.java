package microstamp.step2.enumeration;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum InteractionType {

    CONTROL_ACTION("ControlAction"),
    FEEDBACK("Feedback"),
    COMMUNICATION_CHANNEL("CommunicationChannel"),
    PROCESS_INPUT("ProcessInput"),
    PROCESS_OUTPUT("ProcessOutput"),
    DISTURBANCE("Disturbance");

    private final String formattedName;

    InteractionType(String formattedName) {
        this.formattedName = formattedName;
    }

    public static List<InteractionType> getDefaultTypes() {
        return Arrays.asList(
                CONTROL_ACTION,
                FEEDBACK,
                COMMUNICATION_CHANNEL
        );
    }

    public static List<InteractionType> getSourceEnvironmentTypes() {
        return Arrays.asList(
                PROCESS_INPUT,
                DISTURBANCE
        );
    }

    public static List<InteractionType> getTargetEnvironmentTypes() {
        return List.of(PROCESS_OUTPUT);
    }
}
