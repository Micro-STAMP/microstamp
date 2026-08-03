package microstamp.cast.step4.dto.systemicfactor;

import microstamp.cast.step4.entity.enums.SystemicFactorCategory;

import java.util.List;
import java.util.UUID;

public record SystemicFactorInsertDto(
        UUID analysisId,
        SystemicFactorCategory category,
        String description,
        List<UUID> inadequateControlActionIds
) {}
