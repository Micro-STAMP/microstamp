package microstamp.authorization.mapper;

import microstamp.authorization.dto.AnalysisInsertDto;
import microstamp.authorization.dto.AnalysisReadDto;
import microstamp.authorization.entity.Analysis;

public class AnalysisMapper {

    public static AnalysisReadDto toDto(Analysis analysis) {
        return new AnalysisReadDto(analysis.getId(),
                analysis.getName(),
                analysis.getDescription(),
                analysis.getCreatedAt(),
                analysis.getUser().getId());
    }

    public static Analysis toEntity(AnalysisInsertDto analysisInsertDto) {
        return new Analysis(analysisInsertDto.getName(), analysisInsertDto.getDescription());
    }
}
