package step3.dto.unsafe_control_action;

import lombok.Builder;
import step3.dto.step2.StateReadDto;

import java.util.List;
import java.util.UUID;

@Builder
public record UnsafeControlActionReadDto(
        UUID id,
        UUID analysis_id,
        String name,
        String hazard_code,
        String rule_code,
        String uca_code,
        List<StateReadDto> states,
        String type,
        String constraintName
) {
}
