package step3.dto.variable;

import jakarta.validation.constraints.NotBlank;

public record VariableUpdateDto(
        @NotBlank String name
) {
}
