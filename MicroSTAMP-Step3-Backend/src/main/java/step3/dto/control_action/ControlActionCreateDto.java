package step3.dto.control_action;

import jakarta.validation.constraints.*;

public record ControlActionCreateDto(
        @NotBlank
        String name,
        @NotNull
        Long controller_id
) {}
