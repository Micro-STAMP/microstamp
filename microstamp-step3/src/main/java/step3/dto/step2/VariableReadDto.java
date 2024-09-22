package step3.dto.step2;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record VariableReadDto(
        UUID id,
        String name,
        String code,
        List<StateReadDto> states
) {
}
