package microstamp.cast.step5.dto.recommendation;

import jakarta.validation.constraints.NotBlank;
import microstamp.cast.step5.entity.enums.RecommendationPriority;
import microstamp.cast.step5.entity.enums.RecommendationScopeCategory;
import microstamp.cast.step5.entity.enums.RecommendationStatus;
import microstamp.cast.step5.entity.enums.RecommendationType;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record RecommendationUpdateDto(

        @NotBlank
        String description,

        List<UUID> controlActionContributionIds,
        List<UUID> systemicFactorIds,
        UUID componentId,
        RecommendationPriority priority,
        String auditMechanism,
        String assignee,
        LocalDate dueDate,
        RecommendationStatus status,
        RecommendationType recommendationType,
        RecommendationScopeCategory scopeCategory,
        String effectivenessEvidence
) {}
