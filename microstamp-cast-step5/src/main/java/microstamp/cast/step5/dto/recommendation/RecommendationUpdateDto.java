package microstamp.cast.step5.dto.recommendation;

import java.util.List;
import java.util.UUID;

public record RecommendationUpdateDto(
        String description,
        List<UUID> inadequateControlActionIds,
        List<UUID> systemicFactorIds
) {}
