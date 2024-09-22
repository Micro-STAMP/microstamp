package step3.dto.export;

import lombok.Builder;
import step3.dto.rule.RuleReadListDto;
import step3.dto.unsafe_control_action.UnsafeControlActionReadDto;

import java.util.List;
import java.util.UUID;

@Builder
public record ExportReadDto(
        UUID analysisId,
        List<UnsafeControlActionReadDto> unsafeControlActions,
        List<RuleReadListDto> rules
) {
}
