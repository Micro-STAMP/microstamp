package microstamp.step2.mapper;

import microstamp.step2.dto.state.StateReadDto;
import microstamp.step2.dto.variable.VariableInsertDto;
import microstamp.step2.dto.variable.VariableFullReadDto;
import microstamp.step2.dto.variable.VariableReadDto;
import microstamp.step2.entity.Component;
import microstamp.step2.entity.Variable;

import java.util.Comparator;

public class VariableMapper {

    public static VariableFullReadDto toFullDto(Variable variable){
        return VariableFullReadDto.builder()
                .id(variable.getId())
                .name(variable.getName())
                .code(variable.getCode())
                .states(variable.getStates() != null
                ? variable.getStates().stream()
                    .map(StateMapper::toDto)
                    .sorted(Comparator.comparing(StateReadDto::getCode))
                    .toList()
                : null)
                .createdAt(variable.getCreatedAt())
                .updatedAt(variable.getUpdatedAt())
                .build();
    }

    public static VariableReadDto toDto(Variable variable){
        return VariableReadDto.builder()
                .id(variable.getId())
                .name(variable.getName())
                .code(variable.getCode())
                .createdAt(variable.getCreatedAt())
                .updatedAt(variable.getUpdatedAt())
                .build();
    }

    public static Variable toEntity(VariableInsertDto variableInsertDto, Component component){
        return Variable.builder()
                .name(variableInsertDto.getName())
                .code(variableInsertDto.getCode())
                .component(component)
                .build();
    }
}
