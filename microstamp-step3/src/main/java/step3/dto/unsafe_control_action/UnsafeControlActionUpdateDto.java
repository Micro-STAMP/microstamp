package step3.dto.unsafe_control_action;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UnsafeControlActionUpdateDto(@NotEmpty @NotNull String name) {
}
