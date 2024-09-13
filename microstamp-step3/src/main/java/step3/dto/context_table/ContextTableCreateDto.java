package step3.dto.context_table;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ContextTableCreateDto(
        @NotNull
        UUID control_action_id
) {}
