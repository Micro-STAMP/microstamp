package microstamp.step4.mapper;

import microstamp.step4.dto.fourtuple.FourTupleInsertDto;
import microstamp.step4.dto.fourtuple.FourTupleReadDto;
import microstamp.step4.entity.FourTuple;

public class FourTupleMapper {

    public static FourTupleReadDto toDto(FourTuple fourTuple){
        return FourTupleReadDto.builder()
                .id(fourTuple.getId())
                .code(fourTuple.getCode())
                .scenario(fourTuple.getScenario())
                .associatedCausalFactor(fourTuple.getAssociatedCausalFactor())
                .recommendation(fourTuple.getRecommendation())
                .rationale(fourTuple.getRationale())
                .unsafeControlActionIds(fourTuple.getUnsafeControlActions())
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
