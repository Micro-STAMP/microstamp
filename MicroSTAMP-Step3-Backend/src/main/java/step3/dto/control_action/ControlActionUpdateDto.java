package step3.dto.control_action;

import jakarta.validation.constraints.NotBlank;

public record ControlActionUpdateDto(
        @NotBlank String name
) {
}
