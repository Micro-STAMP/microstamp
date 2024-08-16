package step3.dto.variable;

import step3.entity.Value;
import step3.entity.Variable;
import java.util.List;

public record VariableReadDto(
        Long id,
        String name,
        List<ValueDto> values,
        String controller_name
) {

    // Constructors -----------------------------------

    public VariableReadDto(Variable variable) {
        this(
            variable.getId(),
            variable.getName(),
            variable.getValues().stream().map(ValueDto::new).toList(),
            variable.getController().getName()
        );
    }

    // DTOs -------------------------------------------

    private record ValueDto(Long id, String name) {
        public ValueDto(Value value) {
            this(
                value.getId(),
                value.getName()
            );
        }
    }

    // ------------------------------------------------
}
