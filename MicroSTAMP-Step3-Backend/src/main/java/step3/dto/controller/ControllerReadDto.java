package step3.dto.controller;

import step3.entity.ControlAction;
import step3.entity.Controller;
import step3.entity.Value;
import step3.entity.Variable;
import java.util.List;
import java.util.Optional;

public record ControllerReadDto(
        Long id,
        String name,
        List<ControlActionDto> control_actions,
        List<VariableDto> variables,
        Optional<Long> context_table_id,
        String project_name,
        Long project_id
) {

    // Constructors -----------------------------------

    public ControllerReadDto(Controller controller) {
        this(
           controller.getId(),
           controller.getName(),
           controller.getControlActions().stream().map(ControlActionDto::new).toList(),
           controller.getVariables().stream().map(VariableDto::new).toList(),
           Optional.ofNullable(controller.getContextTable() != null ? controller.getContextTable().getId() : null),
           controller.getProject().getName(),
           controller.getProject().getId()
        );
    }

    // DTOs -------------------------------------------

    private record ControlActionDto(Long id, String name) {
        public ControlActionDto(ControlAction ca) {
            this(
                ca.getId(),
                ca.getName()
            );
        }
    }
    private record ValueDto(Long id, String name) {
        public ValueDto(Value value) {
            this(
                    value.getId(),
                    value.getName()
            );
        }
    }
    private record VariableDto(Long id, String name, List<ValueDto> values) {
        public VariableDto(Variable variable) {
            this(
                variable.getId(),
                variable.getName(),
                variable.getValues().stream().map(ValueDto::new).toList()
            );
        }
    }

    // ------------------------------------------------
}
