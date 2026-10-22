package microstamp.cast.step3.dto.controlactioncontribution;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import microstamp.cast.step3.entity.enums.IcaType;

public record ControlActionContributionUpdateDto(

        @NotBlank
        String controlActionName,

        @NotNull
        IcaType type,

        String description,
        String context,
        String processModelFlaw,
        String questions
) {}
