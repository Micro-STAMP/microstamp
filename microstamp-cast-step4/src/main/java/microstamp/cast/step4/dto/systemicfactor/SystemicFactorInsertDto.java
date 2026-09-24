package microstamp.cast.step4.dto.systemicfactor;

import microstamp.cast.step4.entity.enums.SystemicFactorCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record SystemicFactorInsertDto(

        @NotNull
        UUID analysisId,

        @NotNull
        SystemicFactorCategory category,

        @NotBlank
        String description,

        List<UUID> inadequateControlActionIds
) {}
