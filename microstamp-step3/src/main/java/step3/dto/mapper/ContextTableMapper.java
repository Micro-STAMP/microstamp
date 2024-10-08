package step3.dto.mapper;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import step3.dto.context_table.ContextTableReadDto;
import step3.dto.context_table.ContextTableReadWithPageDto;
import step3.dto.context_table.ContextReadDto;
import step3.dto.step2.ControlActionReadDto;
import step3.dto.step2.StateReadDto;
import step3.entity.Context;
import step3.entity.ContextTable;
import step3.entity.association.ContextState;
import step3.proxy.Step2Proxy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class ContextTableMapper {
    private final Step2Proxy step2Proxy;

    public ContextTableReadDto toContextTableReadDto(ContextTable contextTable) {
        ControlActionReadDto controlAction = step2Proxy.getControlActionById(contextTable.getControlActionId());
        List<Context> contexts = contextTable.getContexts();

        return ContextTableReadDto.builder()
                .id(contextTable.getId())
                .source_id(controlAction.connection().source().id())
                .target_id(controlAction.connection().target().id())
                .contexts(this.generateContextList(contexts))
                .control_action_id(controlAction.id())
                .build();
    }

    public ContextTableReadWithPageDto toContextTableReadWithPageDto(ContextTable contextTable, Page<Context> contexts) {
        ControlActionReadDto controlAction = step2Proxy.getControlActionById(contextTable.getControlActionId());
        List<Context> contextsList = contexts.getContent();

        return ContextTableReadWithPageDto.builder()
                .id(contextTable.getId())
                .source_id(controlAction.connection().source().id())
                .target_id(controlAction.connection().target().id())
                .contexts(this.generateContextList(contextsList))
                .totalPages(contexts.getTotalPages())
                .currentPage(contexts.getNumber())
                .build();
    }

    private List<ContextReadDto> generateContextList(List<Context> contexts) {
        List<ContextReadDto> contextList = new ArrayList<>();

        contexts.forEach(context -> {
            List<UUID> statesIds = context.getStateAssociations().stream()
                    .map(ContextState::getStateId)
                    .toList();
            ContextReadDto contextDto = ContextReadDto.builder()
                    .id(context.getId())
                    .states(this.generateStateList(statesIds))
                    .build();

            contextList.add(contextDto);
        });

        return contextList;
    }

    private List<StateReadDto> generateStateList(List<UUID> statesIds) {
        List<StateReadDto> states = new ArrayList<>();

        statesIds.forEach(stateId -> {
            states.add(step2Proxy.getStateById(stateId));
        });

        return states;
    }
}
