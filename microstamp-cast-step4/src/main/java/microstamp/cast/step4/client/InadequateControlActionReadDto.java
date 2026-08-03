package microstamp.cast.step4.client;

import java.util.UUID;

public record InadequateControlActionReadDto(
        UUID id,
        UUID analysisId,
        UUID componentId,
        String code,
        String controlActionName,
        String type,
        String description,
        String context,
        String processModelFlaw
) {}
