package microstamp.cast.step3.mapper;

import microstamp.cast.step3.dto.controlactioncontribution.ControlActionContributionInsertDto;
import microstamp.cast.step3.dto.controlactioncontribution.ControlActionContributionReadDto;
import microstamp.cast.step3.dto.controlactioncontribution.ControlActionContributionUpdateDto;
import microstamp.cast.step3.entity.ControlActionContribution;
import org.springframework.stereotype.Component;

@Component
public class ControlActionContributionMapper {

    public ControlActionContributionReadDto toReadDto(ControlActionContribution entity) {
        return new ControlActionContributionReadDto(
                entity.getId(),
                entity.getAnalysisId(),
                entity.getComponentId(),
                entity.getCode(),
                entity.getControlActionName(),
                entity.getType(),
                entity.getDescription(),
                entity.getContext(),
                entity.getProcessModelFlaw(),
                entity.getQuestions(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public ControlActionContribution toEntity(ControlActionContributionInsertDto dto) {
        return ControlActionContribution.builder()
                .analysisId(dto.analysisId())
                .componentId(dto.componentId())
                .code(dto.code())
                .controlActionName(dto.controlActionName())
                .type(dto.type())
                .description(dto.description())
                .context(dto.context())
                .processModelFlaw(dto.processModelFlaw())
                .questions(dto.questions())
                .build();
    }

    public void updateEntityFromDto(ControlActionContributionUpdateDto dto, ControlActionContribution entity) {
        entity.setControlActionName(dto.controlActionName());
        entity.setType(dto.type());
        entity.setDescription(dto.description());
        entity.setContext(dto.context());
        entity.setProcessModelFlaw(dto.processModelFlaw());
        entity.setQuestions(dto.questions());
    }
}
