package microstamp.cast.step5.client;

import java.util.List;
import java.util.UUID;

public record SystemicFactorReadDto(
        UUID id,
        UUID analysisId,
        String category,
        String description,
        List<UUID> inadequateControlActionIds
) {}
