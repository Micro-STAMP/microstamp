package step3.dto.controller;

import jakarta.validation.constraints.*;

public record ControllerCreateDto(
        @NotBlank
        String name,
        @NotNull
        Long project_id
) {}
