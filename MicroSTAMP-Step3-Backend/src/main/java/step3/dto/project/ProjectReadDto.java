package step3.dto.project;

import step3.entity.Controller;
import step3.entity.Hazard;
import step3.entity.Project;
import step3.entity.UnsafeControlAction;
import java.util.List;

public record ProjectReadDto(
        Long id,
        String name,
        String description,
        List<HazardDto> hazards,
        List<ControllerDto> controllers,
        List<UnsafeControlActionDto> unsafe_control_actions
) {

    // Constructors -----------------------------------

    public ProjectReadDto(Project project) {
        this(
            project.getId(),
            project.getName(),
            project.getDescription(),
            project.getHazards().stream().map(HazardDto::new).toList(),
            project.getControllers().stream().map(ControllerDto::new).toList(),
            project.getUnsafeControlActions().stream().map(UnsafeControlActionDto::new).toList()
        );
    }

    // DTOs -------------------------------------------

    private record HazardDto(String name, Long id) {
        public HazardDto(Hazard hazard) {
            this(hazard.getName(), hazard.getId());
        }
    }
    private record ControllerDto(String name, Long id) {
        public ControllerDto (Controller controller){
            this(controller.getName(), controller.getId());
        }
    }
    private record UnsafeControlActionDto(String name, Long id) {
        public UnsafeControlActionDto(UnsafeControlAction uca) {
            this(uca.getName(), uca.getId());
        }
    }

    // ------------------------------------------------
}
