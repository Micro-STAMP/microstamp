package step3.dto.step2;

import lombok.Builder;

import java.util.UUID;

@Builder
public record StateReadDto(
        UUID id,
        String name,
        String code,
        VariableStateReadDto variable
) {
}

@Builder
record VariableStateReadDto(
        UUID id,
        String name,
        String code
) {
}
