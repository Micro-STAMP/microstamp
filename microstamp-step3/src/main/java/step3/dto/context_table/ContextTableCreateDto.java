package step3.dto.context_table;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ContextTableCreateDto(
        @NotNull
        UUID control_action_id
) {}
