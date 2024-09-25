package step3.dto.not_uca_context;

import lombok.Builder;
import step3.dto.step2.StateReadDto;

import java.util.List;
import java.util.UUID;

@Builder
public record NotUcaContextReadDto(
        UUID id,
        UUID analysis_id,
        String rule_code,
        List<StateReadDto> states,
        String type
) {
}
