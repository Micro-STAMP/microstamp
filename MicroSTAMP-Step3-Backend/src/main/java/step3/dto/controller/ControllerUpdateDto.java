package step3.dto.controller;

import jakarta.validation.constraints.NotBlank;

public record ControllerUpdateDto(
        @NotBlank String name
) {
}
