package step3.dto.unsafe_control_action;

import lombok.Builder;
import step3.dto.step2.ControlActionReadDto;
import step3.dto.step2.StateReadDto;
import step3.entity.UCAType;

import java.util.List;
import java.util.UUID;

@Builder
public record UnsafeControlActionFullReadDto(
    UUID id,
    UUID analysis_id,
    String name,
    String hazard_code,
    String rule_code,
    String uca_code,
    List<StateReadDto> states,
    UCAType type,
    String constraintName,
    String constraint_code,
    String context,
    ControlActionReadDto control_action
) {
}
