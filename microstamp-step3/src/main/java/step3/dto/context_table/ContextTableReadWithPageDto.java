package step3.dto.context_table;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record ContextTableReadWithPageDto(
        UUID id,
        UUID source_id,
        UUID target_id,
        List<ContextReadDto> contexts
        ) {

    // Constructors -----------------------------------

//    public ContextTableReadWithPageDto(ContextTable contextTable, Page<Context> contexts) {
//        this(
//                contextTable.getId(),
//                contextTable.getControllerId(),
////                contextTable.getController().getName(),
//                contexts.getContent().stream().map(ContextDto::new).toList()
//        );
//    }

//    // DTOs -------------------------------------------
//
//    private record ContextDto(UUID id, List<UUID> statesIds) {
//        public ContextDto(Context context) {
//            this(
//                    context.getId(),
//                    context.getStateAssociations().stream()
//                            .map(ContextState::getStateId)
//                            .toList()
////                    context.getValues().stream().map(ValueDto::new).toList()
//            );
//        }
//
//        private record StateDto(UUID state_id, /*String variable_name,*/ String value_name) {
//            public StateDto(StateReadDto state) {
//                this(
//                        state.id(),
//                        /*value.getVariable().getName(),*/
//                        state.name()
//                );
//            }
//        }
//    }
}
