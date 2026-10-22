package microstamp.cast.step5.client;

import java.util.List;
import java.util.UUID;

public record ControlActionContributionReadDto(
        UUID id,
        UUID analysisId,
        UUID componentId,
        String code,
        String controlActionName,
        String type,
        String description,
        List<String> context,
        List<String> processModelFlaw
) {}
