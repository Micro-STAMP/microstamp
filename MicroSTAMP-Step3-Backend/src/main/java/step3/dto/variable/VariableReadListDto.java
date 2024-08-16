package step3.dto.variable;

import step3.entity.Variable;

public record VariableReadListDto(
    Long id,
    String name,
    String controller_name
) {

    // Constructors -----------------------------------

    public VariableReadListDto(Variable variable) {
        this(
            variable.getId(),
            variable.getName(),
            variable.getController().getName()
        );
    }

    // ------------------------------------------------
}
