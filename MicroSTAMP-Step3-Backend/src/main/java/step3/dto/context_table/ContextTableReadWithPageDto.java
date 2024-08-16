package step3.dto.context_table;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import step3.entity.Context;
import step3.entity.ContextTable;
import step3.entity.Value;

import java.util.List;
import java.util.stream.Collectors;

public record ContextTableReadWithPageDto(
        Long id,
        Long controller_id,
        String controller_name,
        List<ContextDto> contexts
        ) {

    // Constructors -----------------------------------

    public ContextTableReadWithPageDto(ContextTable contextTable, Page<Context> contexts) {
        this(
                contextTable.getId(),
                contextTable.getController().getId(),
                contextTable.getController().getName(),
                contexts.getContent().stream().map(ContextDto::new).toList()
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
}
