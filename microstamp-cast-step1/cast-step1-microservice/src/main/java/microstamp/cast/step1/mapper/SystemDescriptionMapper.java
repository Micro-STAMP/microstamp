package microstamp.cast.step1.mapper;

import microstamp.cast.step1.dto.systemdescription.SystemDescriptionInsertDto;
import microstamp.cast.step1.dto.systemdescription.SystemDescriptionReadDto;
import microstamp.cast.step1.entity.SystemDescription;

public class SystemDescriptionMapper {

    public static SystemDescriptionReadDto toDto(SystemDescription entity) {
        return SystemDescriptionReadDto.builder()
                .id(entity.getId())
                .description(entity.getDescription())
                .analysisBoundary(entity.getAnalysisBoundary())
                .generalDescription(entity.getGeneralDescription())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static SystemDescription toEntity(SystemDescriptionInsertDto dto) {
        return SystemDescription.builder()
                .description(dto.getDescription())
                .analysisBoundary(dto.getAnalysisBoundary())
                .generalDescription(dto.getGeneralDescription())
                .analysisId(dto.getAnalysisId())
                .build();
    }
}