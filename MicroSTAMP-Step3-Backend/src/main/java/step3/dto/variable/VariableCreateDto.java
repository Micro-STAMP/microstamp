package step3.dto.variable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VariableCreateDto(
        @NotBlank
        String name,
        @NotNull
        Long controller_id

) {}
