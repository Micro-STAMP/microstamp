package microstamp.cast.step4.mapper;

import microstamp.cast.step4.dto.systemicfactor.SystemicFactorInsertDto;
import microstamp.cast.step4.dto.systemicfactor.SystemicFactorReadDto;
import microstamp.cast.step4.dto.systemicfactor.SystemicFactorUpdateDto;
import microstamp.cast.step4.entity.SystemicFactor;
import org.springframework.stereotype.Component;

@Component
public class SystemicFactorMapper {

    public SystemicFactorReadDto toReadDto(SystemicFactor entity) {
        return new SystemicFactorReadDto(
                entity.getId(),
                entity.getAnalysisId(),
                entity.getCategory(),
                entity.getDescription(),
                entity.getControlActionContributionIds(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public SystemicFactor toEntity(SystemicFactorInsertDto dto) {
        return SystemicFactor.builder()
                .analysisId(dto.analysisId())
                .category(dto.category())
                .description(dto.description())
                .controlActionContributionIds(dto.controlActionContributionIds())
                .build();
    }

    public void updateEntityFromDto(SystemicFactorUpdateDto dto, SystemicFactor entity) {
        entity.setCategory(dto.category());
        entity.setDescription(dto.description());
        entity.setControlActionContributionIds(dto.controlActionContributionIds());
    }
}
