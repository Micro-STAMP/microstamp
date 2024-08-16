package step3.dto.controller;

import step3.entity.Controller;

public record ControllerReadListDto(
        Long id,
        String name,
        String project_name
) {

    // Constructors -----------------------------------

    public ControllerReadListDto(Controller controller) {
        this(
            controller.getId(),
            controller.getName(),
            controller.getProject().getName()
        );
    }

    // ------------------------------------------------
}
