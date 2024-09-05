package microstamp.step1.mapper;

import microstamp.step1.dto.loss.LossInsertDto;
import microstamp.step1.dto.loss.LossReadDto;
import microstamp.step1.entity.Loss;

public class LossMapper {

    public static LossReadDto toDto(Loss loss){
        return LossReadDto.builder()
                .id(loss.getId())
                .name(loss.getName())
                .code(loss.getCode())
                .build();
    }

    public static Loss toEntity(LossInsertDto lossInsertDto){
        return Loss.builder()
                .name(lossInsertDto.getName())
                .code(lossInsertDto.getCode())
                .analysisId(lossInsertDto.getAnalysisId())
                .build();
    }
}
