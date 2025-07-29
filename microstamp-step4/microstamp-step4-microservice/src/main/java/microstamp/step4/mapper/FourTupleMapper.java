package microstamp.step4.mapper;

import microstamp.step3.dto.unsafecontrolaction.UnsafeControlActionReadDto;
import microstamp.step4.dto.fourtuple.FourTupleInsertDto;
import microstamp.step4.dto.fourtuple.FourTupleFullReadDto;
import microstamp.step4.dto.fourtuple.FourTupleReadDto;
import microstamp.step4.entity.FourTuple;

import java.util.List;
import java.util.UUID;

public class FourTupleMapper {

    public static FourTupleReadDto toDto(FourTuple fourTuple, List<UUID> unsafeControlActionIds){
        return FourTupleReadDto.builder()
                .id(fourTuple.getId())
                .code(fourTuple.getCode())
                .scenario(fourTuple.getScenario())
                .associatedCausalFactor(fourTuple.getAssociatedCausalFactor())
                .recommendation(fourTuple.getRecommendation())
                .rationale(fourTuple.getRationale())
                .unsafeControlActionIds(unsafeControlActionIds)
                .build();
    }

    public static FourTupleFullReadDto toFullDto(FourTuple fourTuple, List<UnsafeControlActionReadDto> unsafeControlActionReadDtos){
        return FourTupleFullReadDto.builder()
                .id(fourTuple.getId())
                .code(fourTuple.getCode())
                .scenario(fourTuple.getScenario())
                .associatedCausalFactor(fourTuple.getAssociatedCausalFactor())
                .recommendation(fourTuple.getRecommendation())
                .rationale(fourTuple.getRationale())
                .unsafeControlActions(unsafeControlActionReadDtos)
                .build();
    }

    public static FourTuple toEntity(FourTupleInsertDto fourTupleInsertDto){
        return FourTuple.builder()
                .scenario(fourTupleInsertDto.getScenario())
                .associatedCausalFactor(fourTupleInsertDto.getAssociatedCausalFactor())
                .recommendation(fourTupleInsertDto.getRecommendation())
                .rationale(fourTupleInsertDto.getRationale())
                .unsafeControlActions(fourTupleInsertDto.getUnsafeControlActionIds())
                .code(fourTupleInsertDto.getCode())
                .analysisId(fourTupleInsertDto.getAnalysisId())
                .build();
    }
}
