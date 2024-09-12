package microstamp.step2.mapper;

import microstamp.step2.dto.environment.EnvironmentReadDto;
import microstamp.step2.dto.variable.VariableFullReadDto;
import microstamp.step2.entity.Environment;

import java.util.Comparator;
import java.util.UUID;

public class EnvironmentMapper {

    public static EnvironmentReadDto toDto(Environment environment) {
        return EnvironmentReadDto.builder()
                .id(environment.getId())
                .name(environment.getName())
                .code(environment.getCode())
                .isVisible(environment.getIsVisible())
                .border(environment.getBorder())
                .variables(environment.getVariables() != null
                    ? environment.getVariables().stream()
                        .map(VariableMapper::toFullDto)
                        .sorted(Comparator.comparing(VariableFullReadDto::getCode))
                        .toList()
                    : null)
                .build();

    }

    public static Environment toEntity(UUID analysisId) {
        return Environment.builder()
                .analysisId(analysisId)
                .build();
    }
}
