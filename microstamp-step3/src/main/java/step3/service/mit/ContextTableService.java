package step3.service.mit;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import step3.dto.mit.context_table.ContextTableCreateDto;
import step3.dto.mit.context_table.ContextTableReadDto;
import step3.dto.mit.context_table.ContextTableReadWithPageDto;
import step3.dto.mit.mapper.ContextTableMapper;
import step3.dto.mit.step2.ComponentReadDto;
import step3.dto.mit.step2.ConnectionReadDto;
import step3.dto.mit.step2.StateReadDto;
import step3.dto.mit.step2.VariableReadDto;
import step3.entity.mit.Context;
import step3.entity.mit.ContextTable;
import step3.infra.exceptions.OperationNotAllowedException;
import step3.proxy.Step2Proxy;
import step3.repository.mit.ContextRepository;
import step3.repository.mit.ContextTableRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ContextTableService {
    private final ContextTableRepository contextTableRepository;
    private final ContextRepository contextRepository;
    private final Step2Proxy step2Proxy;
    private final ContextTableMapper mapper;

    public ContextTableReadDto createContextTable(ContextTableCreateDto contextTableCreateDto) {
        ConnectionReadDto connection = step2Proxy.getConnectionById(contextTableCreateDto.connection_id());

        ComponentReadDto source = connection.source();
        ComponentReadDto target = connection.target();

        List<VariableReadDto> variables = new ArrayList<>();
        variables.addAll(source.variables());
        variables.addAll(target.variables());

        if (variables.isEmpty()) {
            throw new OperationNotAllowedException("Connection must have at least one variable");
        }

        if (variables.stream().anyMatch(variable -> variable.states().isEmpty())) {
            throw new OperationNotAllowedException("Variables must have at least one value");
        }

        if (contextTableRepository.findByConnectionId(connection.id()).isPresent()) {
            throw new OperationNotAllowedException("Connection already has a context table");
        }

        ContextTable contextTable = generateContextTable(variables);
        contextTable.setConnectionId(connection.id());
        ContextTable createContextTable = contextTableRepository.save(contextTable);
        return new ContextTableReadDto(createContextTable);
    }

    public ContextTableReadDto readContextTableById(UUID id) {
        ContextTable contextTable = contextTableRepository.getReferenceById(id);
        return new ContextTableReadDto(contextTable);
    }
    public List<ContextTableReadDto> readAllContextTables() {
        List<ContextTable> contextTables = contextTableRepository.findAll();
        return contextTables.stream().map(ContextTableReadDto::new).toList();
    }

    public ContextTableReadWithPageDto readContextTableByConnectionId(UUID connectionId, int page, int size) {
        ContextTable contextTable = contextTableRepository.findByConnectionId(connectionId)
                .orElseThrow(() -> new EntityNotFoundException("Context table not found with connection id: " + connectionId));
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<Context> contextsPage = contextRepository.findByContextTableId(contextTable.getId(), pageable);

        return mapper.toContextTableReadWithPageDto(contextTable, contextsPage);
    }

    @Transactional
    public void deleteContextTable(UUID id) {
        var contextTable = contextTableRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Not found context table with id: " + id));
        contextTable.getContexts().clear();
        contextTableRepository.save(contextTable);

        contextTableRepository.deleteById(id);
    }

    public ContextTable generateContextTable(List<VariableReadDto> variables) {
        ContextTable contextTable = new ContextTable();
        generateAllContexts(variables, 0, new ArrayList<>(), contextTable);
        return contextTable;
    }

    private void generateAllContexts(
            List<VariableReadDto> variables,
            int index,
            List<StateReadDto> currentStates,
            ContextTable contextTable) {

        if (index == variables.size()) {
            List<UUID> statesId = currentStates.stream().map(StateReadDto::id).toList();
            contextTable.addContext(new Context(statesId));
            return;
        }

        VariableReadDto currentVariable = variables.get(index);
        for (StateReadDto state : currentVariable.states()) {
            List<StateReadDto> updatedStates = new ArrayList<>(currentStates);
            updatedStates.add(state);
            generateAllContexts(variables, index + 1, updatedStates, contextTable);
        }
    }
}
