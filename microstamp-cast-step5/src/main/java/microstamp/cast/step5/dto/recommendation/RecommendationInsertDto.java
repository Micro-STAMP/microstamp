package microstamp.cast.step5.dto.recommendation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record RecommendationInsertDto(

        @NotNull
        UUID analysisId,

        @NotBlank
        String description,

        List<UUID> inadequateControlActionIds,
        List<UUID> systemicFactorIds
) {}
