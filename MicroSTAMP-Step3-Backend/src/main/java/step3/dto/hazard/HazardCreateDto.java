package step3.dto.hazard;

import jakarta.validation.constraints.*;

public record HazardCreateDto(
        @NotBlank
        String name,
        @NotNull
        Long project_id
) {}
