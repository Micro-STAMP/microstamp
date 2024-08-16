package step3.dto.unsafe_control_action;

import step3.entity.UnsafeControlAction;

public record UnsafeControlActionReadDto(
        Long id,
        String name,
        String project_name,
        String hazard_tag,
        String rule_tag
) {

    // Constructors -----------------------------------

    public UnsafeControlActionReadDto(UnsafeControlAction uca) {
        this(
                uca.getId(),
                uca.getName(),
                uca.getProject().getName(),
                uca.getHazard().getTagName(),
                uca.getRuleTag()
        );
    }
}
