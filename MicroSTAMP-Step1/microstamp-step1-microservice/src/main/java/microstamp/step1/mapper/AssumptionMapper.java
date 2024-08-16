package microstamp.step1.mapper;

import microstamp.step1.entity.Assumption;
import microstamp.step1.dto.assumption.AssumptionInsertDto;
import microstamp.step1.dto.assumption.AssumptionReadDto;

public class AssumptionMapper {

    public static AssumptionReadDto toDto(Assumption assumption){
        return AssumptionReadDto.builder()
                .id(assumption.getId())
                .name(assumption.getName())
                .code(assumption.getCode())
                .build();
    }

    public static Assumption toEntity(AssumptionInsertDto assumptionInsertDto){
        return Assumption.builder()
                .name(assumptionInsertDto.getName())
                .code(assumptionInsertDto.getCode())
                .analysisId(assumptionInsertDto.getAnalysisId())
                .build();
    }
}
