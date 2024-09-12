package step3.dto.rule;

import lombok.Builder;
import step3.dto.step1.HazardReadDto;
import step3.dto.step2.ControlActionReadDto;
import step3.dto.step2.StateReadDto;
import step3.entity.Rule;
import step3.entity.UCAType;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Builder
public record RuleReadDto(
        UUID id,
        String name,
        ControlActionReadDto control_action,
        List<StateReadDto> states,
        Set<UCAType> types,
        HazardReadDto hazard,
        String code
) {}
