package microstamp.step2.enumeration;

import lombok.Getter;

@Getter
public enum ComponentType {

    CONTROLLER("Controller"),
    CONTROLLED_PROCESS("ControlledProcess"),
    ACTUATOR("Actuator"),
    SENSOR("Sensor");

    private final String formattedName;

    ComponentType(String formattedName) {
        this.formattedName = formattedName;
    }
}
