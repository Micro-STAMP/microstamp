package step3.dto.mit.step2;

import java.util.List;
import java.util.UUID;

public record VariableReadDto(
        UUID id,
        String name,
        String code,
        List<StateReadDto> states
) {
}
