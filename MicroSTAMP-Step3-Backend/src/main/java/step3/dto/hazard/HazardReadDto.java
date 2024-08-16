package step3.dto.hazard;

import step3.entity.Hazard;

public record HazardReadDto(
        Long id,
        String name,
        String project_name,
        String tag_name
) {

    // Constructors -----------------------------------

    public HazardReadDto(Hazard hazard) {
        this(
                hazard.getId(),
                hazard.getName(),
                hazard.getProject().getName(),
                hazard.getTagName()
        );
    }

    // ------------------------------------------------
}
