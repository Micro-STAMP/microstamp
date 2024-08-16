package step3.dto.context_table;

import jakarta.validation.constraints.*;

public record ContextTableCreateDto(
        @NotNull
        Long controller_id
) {}
