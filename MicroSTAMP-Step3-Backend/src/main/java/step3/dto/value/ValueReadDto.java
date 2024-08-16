package step3.dto.value;

import step3.entity.Value;

public record ValueReadDto(
        Long id,
        String name,
        String variable_name
) {

    // Constructors -----------------------------------

    public ValueReadDto(Value value) {
        this(
            value.getId(),
            value.getName(),
            value.getVariable().getName()
        );
    }

    // ------------------------------------------------
}
