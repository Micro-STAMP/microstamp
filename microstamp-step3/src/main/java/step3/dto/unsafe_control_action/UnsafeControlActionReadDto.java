package step3.dto.unsafe_control_action;

import lombok.Builder;
import step3.dto.step2.StateReadDto;

import java.util.List;
import java.util.UUID;

@Builder
public record UnsafeControlActionReadDto(
        UUID id,
        UUID analysisId,
        String name,
        String hazardCode,
        String ruleCode,
        List<StateReadDto> states,
        String type,
        String constraintName
) {
}
