package microstamp.cast.step3.dto.controlactioncontribution;

import microstamp.cast.step3.entity.enums.IcaType;
import java.time.Instant;
import java.util.UUID;

public record ControlActionContributionReadDto(
        UUID id,
        UUID analysisId,
        UUID componentId,
        String code,
        String controlActionName,
        IcaType type,
        String description,
        String context,
        String processModelFlaw,
        String questions,
        Instant createdAt,
        Instant updatedAt
) {}
