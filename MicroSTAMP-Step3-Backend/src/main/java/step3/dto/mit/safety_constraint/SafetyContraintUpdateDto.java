package step3.dto.mit.safety_constraint;

import jakarta.validation.constraints.NotBlank;

public record SafetyContraintUpdateDto(
        @NotBlank String name
) {
}