package microstamp.cast.step4.dto.systemicfactor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import microstamp.cast.step4.entity.enums.SystemicFactorCategory;

import java.util.List;
import java.util.UUID;

public record SystemicFactorUpdateDto(

        @NotNull
        SystemicFactorCategory category,

        @NotBlank
        String description,

        List<UUID> inadequateControlActionIds
) {}
