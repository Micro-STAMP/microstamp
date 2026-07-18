package microstamp.cast.step3.dto.inadequatecontrolaction;

import microstamp.cast.step3.entity.enums.IcaType;

public record InadequateControlActionUpdateDto(
        String controlActionName,
        IcaType type,
        String description,
        String context,
        String processModelFlaw
) {}