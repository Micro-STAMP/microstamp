package microstamp.cast.step5.dto.recommendation;

import microstamp.cast.step5.entity.enums.RecommendationPriority;

import java.util.List;
import java.util.UUID;

public record RecommendationUpdateDto(
        String description,
        List<UUID> inadequateControlActionIds,
        List<UUID> systemicFactorIds,
        UUID componentId,
        RecommendationPriority priority,
        String auditMechanism
) {}
