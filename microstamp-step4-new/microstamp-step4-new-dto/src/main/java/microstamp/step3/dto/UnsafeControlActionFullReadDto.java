package microstamp.step3.dto;

import lombok.Builder;
import microstamp.step2.dto.ControlActionReadDto;
import microstamp.step2.dto.StateReadDto;

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
