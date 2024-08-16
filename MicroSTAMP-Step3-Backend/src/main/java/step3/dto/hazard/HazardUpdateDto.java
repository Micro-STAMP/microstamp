package step3.dto.hazard;

import jakarta.validation.constraints.NotBlank;

public record HazardUpdateDto(
        @NotBlank String name
) {
}
