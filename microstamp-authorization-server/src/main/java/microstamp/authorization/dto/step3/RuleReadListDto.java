package microstamp.authorization.dto.step3;

import lombok.Builder;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Builder
public record RuleReadListDto(
        UUID id,
        String name,
        String control_action_name,
        List<StateReadDto> states,
        Set<UCAType> types,
        HazardReadDto hazard,
        String code
) {}
