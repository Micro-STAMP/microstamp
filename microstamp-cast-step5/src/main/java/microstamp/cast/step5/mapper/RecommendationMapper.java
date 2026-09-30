package microstamp.cast.step5.mapper;

import microstamp.cast.step5.dto.recommendation.RecommendationInsertDto;
import microstamp.cast.step5.dto.recommendation.RecommendationReadDto;
import microstamp.cast.step5.dto.recommendation.RecommendationUpdateDto;
import microstamp.cast.step5.entity.Recommendation;
import org.springframework.stereotype.Component;

@Component
public class RecommendationMapper {

    public RecommendationReadDto toReadDto(Recommendation entity) {
        return new RecommendationReadDto(
                entity.getId(),
                entity.getAnalysisId(),
                entity.getDescription(),
                entity.getInadequateControlActionIds(),
                entity.getSystemicFactorIds(),
                entity.getComponentId(),
                entity.getPriority(),
                entity.getAuditMechanism()
        );
    }

    public Recommendation toEntity(RecommendationInsertDto dto) {
        return Recommendation.builder()
                .analysisId(dto.analysisId())
                .description(dto.description())
                .inadequateControlActionIds(dto.inadequateControlActionIds())
                .systemicFactorIds(dto.systemicFactorIds())
                .componentId(dto.componentId())
                .priority(dto.priority())
                .auditMechanism(dto.auditMechanism())
                .build();
    }

    public void updateEntityFromDto(RecommendationUpdateDto dto, Recommendation entity) {
        entity.setDescription(dto.description());
        entity.setInadequateControlActionIds(dto.inadequateControlActionIds());
        entity.setSystemicFactorIds(dto.systemicFactorIds());
        entity.setComponentId(dto.componentId());
        entity.setPriority(dto.priority());
        entity.setAuditMechanism(dto.auditMechanism());
    }
}
