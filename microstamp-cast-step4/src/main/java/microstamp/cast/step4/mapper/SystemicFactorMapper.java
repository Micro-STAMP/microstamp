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
                entity.getCustomCategory(),
                entity.getDescription(),
                entity.getControlActionContributionIds(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public SystemicFactor toEntity(SystemicFactorInsertDto dto) {
        return SystemicFactor.builder()
                .analysisId(dto.analysisId())
                .category(isCustom(dto.customCategory()) ? null : dto.category())
                .customCategory(isCustom(dto.customCategory()) ? dto.customCategory().trim() : null)
                .description(dto.description())
                .controlActionContributionIds(dto.controlActionContributionIds())
                .build();
    }

    public void updateEntityFromDto(SystemicFactorUpdateDto dto, SystemicFactor entity) {
        entity.setCategory(isCustom(dto.customCategory()) ? null : dto.category());
        entity.setCustomCategory(isCustom(dto.customCategory()) ? dto.customCategory().trim() : null);
        entity.setDescription(dto.description());
        entity.setControlActionContributionIds(dto.controlActionContributionIds());
    }

    private boolean isCustom(String customCategory) {
        return customCategory != null && !customCategory.isBlank();
    }
}
