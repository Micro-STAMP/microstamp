package step3.dto.mit.unsafe_control_action;

import lombok.Builder;
import step3.entity.mit.UnsafeControlAction;

import java.util.UUID;

@Builder
public record UnsafeControlActionReadDto(
        UUID id,
        String name,
        String hazard_code,
        String rule_code
) {

    // Constructors -----------------------------------

//    public UnsafeControlActionReadDto(UnsafeControlAction uca) {
//        this(
//                uca.getId(),
//                uca.getName(),
//                uca.getProject().getName(),
//                uca.getHazard().getTagName(),
//                uca.getRuleTag()
//        );
//    }
}
