package step3.dto.step2;

import java.util.UUID;

public record StateReadDto(
        UUID id,
        String name,
        String code,
        VariableStateReadDto variable
) {
}

record VariableStateReadDto(
        UUID id,
        String name,
        String code
) {
}
