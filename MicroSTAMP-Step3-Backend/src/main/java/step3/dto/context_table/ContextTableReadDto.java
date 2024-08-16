package step3.dto.context_table;

import step3.entity.Context;
import step3.entity.ContextTable;
import step3.entity.Value;

import java.util.List;

public record ContextTableReadDto(
        Long id,
        List<ContextDto> contexts,
        Long controller_id,
        String controller_name
) {

    // Constructors -----------------------------------

    public ContextTableReadDto(ContextTable contextTable) {
        this(
            contextTable.getId(),
            contextTable.getContexts().stream().map(ContextDto::new).toList(),
            contextTable.getController().getId(),
            contextTable.getController().getName()
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