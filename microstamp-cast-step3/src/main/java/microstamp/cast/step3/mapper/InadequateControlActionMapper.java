package microstamp.cast.step3.mapper;

import microstamp.cast.step3.dto.inadequatecontrolaction.InadequateControlActionInsertDto;
import microstamp.cast.step3.dto.inadequatecontrolaction.InadequateControlActionReadDto;
import microstamp.cast.step3.dto.inadequatecontrolaction.InadequateControlActionUpdateDto;
import microstamp.cast.step3.entity.InadequateControlAction;
import org.springframework.stereotype.Component;

@Component
public class InadequateControlActionMapper {

    public InadequateControlActionReadDto toReadDto(InadequateControlAction entity) {
        return new InadequateControlActionReadDto(
                entity.getId(),
                entity.getAnalysisId(),
                entity.getComponentId(),
                entity.getCode(),
                entity.getControlActionName(),
                entity.getType(),
                entity.getDescription(),
                entity.getContext(),
                entity.getProcessModelFlaw()
        );
    }

    public InadequateControlAction toEntity(InadequateControlActionInsertDto dto) {
        return InadequateControlAction.builder()
                .analysisId(dto.analysisId())
                .componentId(dto.componentId())
                .code(dto.code())
                .controlActionName(dto.controlActionName())
                .type(dto.type())
                .description(dto.description())
                .context(dto.context())
                .processModelFlaw(dto.processModelFlaw())
                .build();
    }

    public void updateEntityFromDto(InadequateControlActionUpdateDto dto, InadequateControlAction entity) {
        entity.setControlActionName(dto.controlActionName());
        entity.setType(dto.type());
        entity.setDescription(dto.description());
        entity.setContext(dto.context());
        entity.setProcessModelFlaw(dto.processModelFlaw());
    }
}