package step3.dto.context_table;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record ContextTableReadDto(
        UUID id,
        UUID sourceId,
        UUID targetId,
        List<ContextReadDto> contexts,
        UUID controlActionId
) {
}