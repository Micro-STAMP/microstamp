package step3.dto.project;

import step3.entity.Project;

public record ProjectReadListDto(
        Long id,
        String name,
        String description
) {

    // Constructors -----------------------------------

    public ProjectReadListDto(Project project) {
        this(
            project.getId(),
            project.getName(),
            project.getDescription()
        );
    }

    // ------------------------------------------------
}
