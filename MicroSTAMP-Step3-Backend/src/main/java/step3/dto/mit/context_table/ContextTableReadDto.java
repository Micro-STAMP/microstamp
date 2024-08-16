package step3.dto.mit.context_table;

import step3.entity.mit.Context;
import step3.entity.mit.ContextTable;

import java.util.List;
import java.util.UUID;

public record ContextTableReadDto(
        UUID id,
        List<ContextDto> contexts,
        UUID controller_id
) {

    // Constructors -----------------------------------

    public ContextTableReadDto(ContextTable contextTable) {
        this(
            contextTable.getId(),
            contextTable.getContexts().stream().map(ContextDto::new).toList(),
            contextTable.getControllerId()
        );
    }

    // DTOs -------------------------------------------

    private record ContextDto(Long id, List<ValueDto> values) {
        public ContextDto(Context context) {
            this(
                context.getId(),
                context.getValues().stream().map(ValueDto::new).toList()
            );
        }
        private record ValueDto(Long value_id, String variable_name, String value_name) {
            public ValueDto(Value value) {
                this(
                    value.getId(),
                    value.getVariable().getName(),
                    value.getName()
                );
            }
        }
    }

    // ------------------------------------------------
}