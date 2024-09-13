package microstamp.authorization.dto.step3;

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
