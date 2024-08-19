package microstamp.step2.mapper;

import microstamp.step2.dto.component.ComponentInsertDto;
import microstamp.step2.dto.component.ComponentReadDto;
import microstamp.step2.dto.responsibility.ResponsibilityReadDto;
import microstamp.step2.dto.variable.VariableReadDto;
import microstamp.step2.entity.*;

import java.util.Comparator;

public class ComponentMapper {

    public static ComponentReadDto toDto(Component component){
        return ComponentReadDto.builder()
                .id(component.getId())
                .name(component.getName())
                .code(component.getCode())
                .isVisible(component.getIsVisible())
                .father(component.getFather() != null
                    ? ComponentMapper.toDto(component.getFather())
                    : null)
                .border(component.getBorder())
                .type(component.getClass().getSimpleName())
                .responsibilities(component.getResponsibilities() != null
                    ? component.getResponsibilities().stream()
                        .map(ResponsibilityMapper::toDto)
                        .sorted(Comparator.comparing(ResponsibilityReadDto::getCode))
                        .toList()
                    : null)
                .variables(component.getVariables() != null
                    ? component.getVariables().stream()
                        .map(VariableMapper::toDto)
                        .sorted(Comparator.comparing(VariableReadDto::getCode))
                        .toList()
                    : null)
                .build();
    }

    public static Component toEntity(ComponentInsertDto componentInsertDto, Component father){
        microstamp.step2.entity.Component component = switch (componentInsertDto.getType()){
            case CONTROLLER -> new Controller();
            case CONTROLLED_PROCESS -> new ControlledProcess();
            case ACTUATOR -> new Actuator();
            case SENSOR -> new Sensor();
        };

        component.setName(componentInsertDto.getName());
        component.setCode(componentInsertDto.getCode());
        component.setBorder(componentInsertDto.getBorder());
        component.setIsVisible(componentInsertDto.getIsVisible());
        component.setAnalysisId(componentInsertDto.getAnalysisId());
        component.setFather(father);

        return component;
    }
}
