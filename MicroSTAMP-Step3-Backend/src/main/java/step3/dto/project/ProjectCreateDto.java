package step3.dto.project;

import jakarta.validation.constraints.*;

public record ProjectCreateDto(
        @NotBlank
        String name,
        @NotNull
        String description
) {}
