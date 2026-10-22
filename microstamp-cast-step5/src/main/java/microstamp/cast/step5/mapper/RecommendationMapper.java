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
                entity.getControlActionContributionIds(),
                entity.getSystemicFactorIds(),
                entity.getComponentId(),
                entity.getPriority(),
                entity.getAuditMechanism(),
                entity.getAssignee(),
                entity.getDueDate(),
                entity.getStatus(),
                entity.getRecommendationType(),
                entity.getScopeCategory(),
                entity.getEffectivenessEvidence(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public Recommendation toEntity(RecommendationInsertDto dto) {
        Recommendation.RecommendationBuilder builder = Recommendation.builder()
                .analysisId(dto.analysisId())
                .description(dto.description())
                .controlActionContributionIds(dto.controlActionContributionIds())
                .systemicFactorIds(dto.systemicFactorIds())
                .componentId(dto.componentId())
                .priority(dto.priority())
                .auditMechanism(dto.auditMechanism())
                .assignee(dto.assignee())
                .dueDate(dto.dueDate())
                .recommendationType(dto.recommendationType())
                .scopeCategory(dto.scopeCategory())
                .effectivenessEvidence(dto.effectivenessEvidence());
        if (dto.status() != null) {
            builder.status(dto.status());
        }
        return builder.build();
    }

    public void updateEntityFromDto(RecommendationUpdateDto dto, Recommendation entity) {
        entity.setDescription(dto.description());
        entity.setControlActionContributionIds(dto.controlActionContributionIds());
        entity.setSystemicFactorIds(dto.systemicFactorIds());
        entity.setComponentId(dto.componentId());
        entity.setPriority(dto.priority());
        entity.setAuditMechanism(dto.auditMechanism());
        entity.setAssignee(dto.assignee());
        entity.setDueDate(dto.dueDate());
        if (dto.status() != null) {
            entity.setStatus(dto.status());
        }
        entity.setRecommendationType(dto.recommendationType());
        entity.setScopeCategory(dto.scopeCategory());
        entity.setEffectivenessEvidence(dto.effectivenessEvidence());
    }
}
