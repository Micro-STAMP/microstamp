package microstamp.cast.step3.dto.inadequatecontrolaction;

import microstamp.cast.step3.entity.enums.IcaType;
import java.util.UUID;

public record InadequateControlActionInsertDto(
        UUID analysisId,
        UUID componentId,
        String code,
        String controlActionName,
        IcaType type,
        String description,
        String context,
        String processModelFlaw
) {}