package step3.dto.mit.mapper;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import step3.dto.mit.context_table.ContextReadDto;
import step3.dto.mit.context_table.ContextTableReadWithPageDto;
import step3.dto.mit.step2.ComponentReadDto;
import step3.dto.mit.step2.StateReadDto;
import step3.entity.mit.Context;
import step3.entity.mit.ContextTable;
import step3.entity.mit.association.ContextState;
import step3.proxy.Step2Proxy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class ContextTableMapper {
    private final Step2Proxy step2Proxy;

    public ContextTableReadWithPageDto toContextTableReadWithPageDto(ContextTable contextTable, Page<Context> contexts) {
        ComponentReadDto controller = step2Proxy.getControllerById(contextTable.getControllerId());
        List<Context> contextsList = contexts.getContent();

        return ContextTableReadWithPageDto.builder()
                .id(contextTable.getId())
                .controller_id(contextTable.getControllerId())
                .controller_name(controller.name())
                .contexts(this.generateContextList(contextsList))
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
