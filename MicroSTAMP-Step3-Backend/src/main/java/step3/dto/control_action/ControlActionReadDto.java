package step3.dto.control_action;

import step3.entity.ControlAction;

public record ControlActionReadDto(
        Long id,
        String name,
        Long controller_id,
        String controller_name,
        Long project_id
) {

    // Constructors -----------------------------------

    public ControlActionReadDto(ControlAction controlAction) {
        this(
            controlAction.getId(),
            controlAction.getName(),
            controlAction.getController().getId(),
            controlAction.getController().getName(),
            controlAction.getController().getProject().getId()
        );
    }

    // ------------------------------------------------
}
