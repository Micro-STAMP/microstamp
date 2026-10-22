package microstamp.cast.step3.dto.controlactioncontribution;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import microstamp.cast.step3.entity.enums.IcaType;

import java.util.List;
import java.util.UUID;

public record ControlActionContributionInsertDto(

        @NotNull
        UUID analysisId,

        @NotNull
        UUID componentId,

        @NotBlank
        String code,

        @NotBlank
        String controlActionName,

        @NotNull
        IcaType type,

        String description,
        List<String> context,
        List<String> processModelFlaw,
        List<String> questions
) {}
