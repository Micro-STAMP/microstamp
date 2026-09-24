package microstamp.cast.step5.dto.recommendation;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

public record RecommendationUpdateDto(

        @NotBlank
        String description,

        List<UUID> inadequateControlActionIds,
        List<UUID> systemicFactorIds
) {}
