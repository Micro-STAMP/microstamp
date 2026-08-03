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
                entity.getInadequateControlActionIds()
        );
    }

    public SystemicFactor toEntity(SystemicFactorInsertDto dto) {
        return SystemicFactor.builder()
                .analysisId(dto.analysisId())
                .category(dto.category())
                .description(dto.description())
                .inadequateControlActionIds(dto.inadequateControlActionIds())
                .build();
    }

    public void updateEntityFromDto(SystemicFactorUpdateDto dto, SystemicFactor entity) {
        entity.setCategory(dto.category());
        entity.setDescription(dto.description());
        entity.setInadequateControlActionIds(dto.inadequateControlActionIds());
    }
}
