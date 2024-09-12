package step3.dto.context_table;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record ContextTableReadWithPageDto(
        UUID id,
        UUID source_id,
        UUID target_id,
        List<ContextReadDto> contexts
        ) { }
