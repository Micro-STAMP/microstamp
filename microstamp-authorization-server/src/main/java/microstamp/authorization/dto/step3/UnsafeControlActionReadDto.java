package microstamp.authorization.dto.step3;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record UnsafeControlActionReadDto(
        UUID id,
        UUID analysis_id,
        String name,
        String hazard_code,
        String rule_code,
        List<StateReadDto> states,
        String type,
        String constraintName
) {
}
