package microstamp.step2.mapper;

import microstamp.step2.dto.state.StateInsertDto;
import microstamp.step2.dto.state.StateReadDto;
import microstamp.step2.entity.State;
import microstamp.step2.entity.Variable;

public class StateMapper {

    public static StateReadDto toDto(State state){
        return StateReadDto.builder()
                .id(state.getId())
                .name(state.getName())
                .code(state.getCode())
                .build();
    }

    public static State toEntity(StateInsertDto stateInsertDto, Variable variable){
        return State.builder()
                .name(stateInsertDto.getName())
                .code(stateInsertDto.getCode())
                .variable(variable)
                .build();
    }
}
