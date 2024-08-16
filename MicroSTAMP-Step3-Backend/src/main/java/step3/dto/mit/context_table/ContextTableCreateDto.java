package step3.dto.mit.context_table;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ContextTableCreateDto(
        @NotNull
        UUID controller_id
) {}
