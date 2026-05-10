package microstamp.cast.step1.mapper;

import microstamp.cast.step1.dto.accidentlossevent.AccidentLossEventInsertDto;
import microstamp.cast.step1.dto.accidentlossevent.AccidentLossEventReadDto;
import microstamp.cast.step1.entity.AccidentLossEvent;

public class AccidentLossEventMapper {

    public static AccidentLossEventReadDto toDto(AccidentLossEvent entity) {
        return AccidentLossEventReadDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }

    public static AccidentLossEvent toEntity(AccidentLossEventInsertDto dto) {
        return AccidentLossEvent.builder()
                .code(dto.getCode())
                .name(dto.getName())
                .description(dto.getDescription())
                .analysisId(dto.getAnalysisId())
                .build();
    }
}