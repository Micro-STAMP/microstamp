package step3.dto.safety_constraint;

import jakarta.validation.constraints.NotBlank;

public record SafetyConstraintUpdateDto(
        @NotBlank String name
) {
}
