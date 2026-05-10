package microstamp.cast.step1.mapper;

import microstamp.cast.step1.dto.casthazard.CastHazardInsertDto;
import microstamp.cast.step1.dto.casthazard.CastHazardReadDto;
import microstamp.cast.step1.entity.CastHazard;

import java.util.Comparator;

public class CastHazardMapper {

    public static CastHazardReadDto toDto(CastHazard entity) {
        return CastHazardReadDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .accidentLossEvents(entity.getAccidentLossEvents() != null
                        ? entity.getAccidentLossEvents().stream()
                        .map(AccidentLossEventMapper::toDto)
                        .sorted(Comparator.comparing(accident -> accident.getCode()))
                        .toList()
                        : null)
                .build();
    }

    public static CastHazard toEntity(CastHazardInsertDto dto) {
        return CastHazard.builder()
                .code(dto.getCode())
                .name(dto.getName())
                .description(dto.getDescription())
                .analysisId(dto.getAnalysisId())
                .build();
    }
}