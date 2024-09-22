package step3.dto.step2;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record ComponentReadDto(
        UUID id,
        String name,
        Object father,
        Object border,
        String code,
        String type,
        List<Object> responsibilities,
        List<VariableReadDto> variables,
        List<ControlActionReadDto> controlActions
) {
}
