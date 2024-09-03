package step3.dto.mit.step2;

import java.util.List;
import java.util.UUID;

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
