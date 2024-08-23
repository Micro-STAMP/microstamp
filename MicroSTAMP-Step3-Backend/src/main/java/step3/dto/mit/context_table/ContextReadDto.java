package step3.dto.mit.context_table;

import lombok.Builder;
import step3.dto.mit.step2.StateReadDto;

import java.util.List;
import java.util.UUID;

@Builder
public record ContextReadDto(
        UUID id,
        List<StateReadDto> states
) {
}
