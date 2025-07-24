package step3.dto.unsafe_control_action;

import jakarta.validation.constraints.NotBlank;

public record UcaCodeUpdateDto(@NotBlank String code) {
}
