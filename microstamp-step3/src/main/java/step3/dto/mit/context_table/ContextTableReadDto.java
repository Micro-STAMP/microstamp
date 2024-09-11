package step3.dto.mit.context_table;

import step3.dto.mit.step2.StateReadDto;
import step3.entity.mit.Context;
import step3.entity.mit.ContextTable;
import step3.entity.mit.association.ContextState;

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
            contextTable.getConnectionId()
        );
    }

    // DTOs -------------------------------------------

    private record ContextDto(UUID id, List<UUID> statesIds) {
        public ContextDto(Context context) {
            this(
                    context.getId(),
                    context.getStateAssociations().stream()
                            .map(ContextState::getStateId)
                            .toList()
//                    context.getValues().stream().map(ValueDto::new).toList()
            );
        }

        private record StateDto(UUID state_id, /*String variable_name,*/ String value_name) {
            public StateDto(StateReadDto state) {
                this(
                        state.id(),
                        /*value.getVariable().getName(),*/
                        state.name()
                );
            }
        }
    }
}