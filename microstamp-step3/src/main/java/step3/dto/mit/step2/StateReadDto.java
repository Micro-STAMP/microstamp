package step3.dto.mit.step2;

import java.util.UUID;

public record StateReadDto(
        UUID id,
        String name,
        String code,
        VariableStateReadDto variableState
) {
}

record VariableStateReadDto(
        UUID id,
        String name,
        String code
) {
}
