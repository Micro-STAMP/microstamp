package microstamp.cast.step3.dto.controlactioncontribution;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import microstamp.cast.step3.entity.enums.IcaType;

import java.util.List;

public record ControlActionContributionUpdateDto(

        @NotBlank
        String controlActionName,

        @NotNull
        IcaType type,

        String description,
        List<String> context,
        List<String> processModelFlaw,
        List<String> questions
) {}
