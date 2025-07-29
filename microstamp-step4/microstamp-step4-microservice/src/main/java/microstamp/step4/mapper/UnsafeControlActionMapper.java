package microstamp.step4.mapper;

import microstamp.step3.dto.unsafecontrolaction.UnsafeControlActionReadDto;
import microstamp.step4.dto.fourtuple.FourTupleReadDto;
import microstamp.step4.dto.unsafecontrolaction.UnsafeControlActionFullReadDto;

import java.util.List;

public class UnsafeControlActionMapper {

    public static UnsafeControlActionFullReadDto toFullDto(UnsafeControlActionReadDto unsafeControlActionReadDto, List<FourTupleReadDto> fourTupleReadDtos){
        return UnsafeControlActionFullReadDto.builder()
                .id(unsafeControlActionReadDto.getId())
                .analysis_id(unsafeControlActionReadDto.getAnalysis_id())
                .name(unsafeControlActionReadDto.getName())
                .uca_code(unsafeControlActionReadDto.getUca_code())
                .hazard_code(unsafeControlActionReadDto.getHazard_code())
                .rule_code(unsafeControlActionReadDto.getRule_code())
                .states(unsafeControlActionReadDto.getStates())
                .type(unsafeControlActionReadDto.getType())
                .constraintName(unsafeControlActionReadDto.getConstraintName())
                .fourTuples(fourTupleReadDtos)
                .build();
    }
}
