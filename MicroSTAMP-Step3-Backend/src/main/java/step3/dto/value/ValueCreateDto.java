package step3.dto.value;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ValueCreateDto(
        @NotBlank
        String name,
        @NotNull
        Long variable_id
) {}
