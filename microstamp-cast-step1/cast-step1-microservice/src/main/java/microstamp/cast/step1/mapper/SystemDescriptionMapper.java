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
                .build();
    }

    public static SystemDescription toEntity(SystemDescriptionInsertDto dto) {
        return SystemDescription.builder()
                .description(dto.getDescription())
                .analysisBoundary(dto.getAnalysisBoundary())
                .analysisId(dto.getAnalysisId())
                .build();
    }
}