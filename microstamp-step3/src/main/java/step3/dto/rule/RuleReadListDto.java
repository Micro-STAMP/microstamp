package step3.dto.rule;

import lombok.Builder;
import step3.dto.step1.HazardReadDto;
import step3.dto.step2.StateReadDto;
import step3.entity.UCAType;

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
