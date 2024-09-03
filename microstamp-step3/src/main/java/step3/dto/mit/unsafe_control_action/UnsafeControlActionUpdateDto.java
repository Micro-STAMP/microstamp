package step3.dto.mit.unsafe_control_action;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UnsafeControlActionUpdateDto(@NotEmpty @NotNull String name) {
}
