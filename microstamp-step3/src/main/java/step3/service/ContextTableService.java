package step3.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import step3.dto.context_table.ContextTableCreateDto;
import step3.dto.context_table.ContextTableReadDto;
import step3.dto.context_table.ContextTableReadWithPageDto;
import step3.dto.mapper.ContextTableMapper;
import step3.dto.step2.ComponentReadDto;
import step3.dto.step2.ControlActionReadDto;
import step3.dto.step2.StateReadDto;
import step3.dto.step2.VariableReadDto;
import step3.entity.Context;
import step3.entity.ContextTable;
import step3.entity.association.ContextState;
import step3.infra.exceptions.OperationNotAllowedException;
import step3.proxy.Step2Proxy;
import step3.repository.ContextRepository;
import step3.repository.ContextTableRepository;
import step3.repository.RuleRepository;
import step3.repository.UnsafeControlActionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ContextTableService {
    private final ContextTableRepository contextTableRepository;
    private final ContextRepository contextRepository;
    private final UnsafeControlActionRepository ucaRepository;
    private final RuleRepository ruleRepository;
    private final Step2Proxy step2Proxy;
    private final ContextTableMapper mapper;

    public ContextTableReadDto createContextTable(ContextTableCreateDto contextTableCreateDto) {
        ControlActionReadDto controlAction = step2Proxy.getControlActionById(contextTableCreateDto.control_action_id());

        ComponentReadDto source = controlAction.connection().source();
        ComponentReadDto target = controlAction.connection().target();

        List<VariableReadDto> variables = new ArrayList<>();
        variables.addAll(source.variables());
        variables.addAll(target.variables());

        if (variables.isEmpty()) {
            throw new OperationNotAllowedException("Components must have at least one variable");
        }

        if (variables.stream().anyMatch(variable -> variable.states().isEmpty())) {
            throw new OperationNotAllowedException("Variables must have at least one value");
        }

        if (contextTableRepository.findByControlActionId(controlAction.id()).isPresent()) {
            throw new OperationNotAllowedException("Connection already has a context table");
        }

        ContextTable contextTable = generateContextTable(variables);
        contextTable.setControlActionId(controlAction.id());
        ContextTable createContextTable = contextTableRepository.save(contextTable);
        return mapper.toContextTableReadDto(createContextTable);
    }

    public ContextTableReadWithPageDto readContextTableById(UUID id, int page, int size) {
        ContextTable contextTable = contextTableRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found context table with id: " + id));

        this.verifyChangesInStates(contextTable);

        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<Context> contextsPage = contextRepository.findByContextTableId(contextTable.getId(), pageable);

        return mapper.toContextTableReadWithPageDto(contextTable, contextsPage);
    }

    public List<ContextTableReadDto> readAllContextTables() {
        List<ContextTable> contextTables = contextTableRepository.findAll();

        return contextTables.stream()
                .map(mapper::toContextTableReadDto)
                .toList();
    }

    public ContextTableReadWithPageDto readContextTableByControlActionId(UUID controlActionId, int page, int size) {
        step2Proxy.getControlActionById(controlActionId);

        ContextTable contextTable = contextTableRepository
                .findByControlActionId(controlActionId)
                .orElseThrow(() -> new EntityNotFoundException("Context table not found with control action id: " + controlActionId));

        this.verifyChangesInStates(contextTable);

        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<Context> contextsPage = contextRepository.findByContextTableId(contextTable.getId(), pageable);

        return mapper.toContextTableReadWithPageDto(contextTable, contextsPage);
    }

    @Transactional
    public void deleteContextTable(UUID id) {
        ContextTable contextTable = contextTableRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found context table with id: " + id));

        ruleRepository.deleteAllByControlActionId(contextTable.getControlActionId());
        ucaRepository.deleteByControlActionId(contextTable.getControlActionId());
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

    public void verifyChangesInStates(ContextTable contextTable) {
        Set<UUID> statesIdsOfContextTable = contextTable.getContexts().stream()
                .flatMap(context -> context.getStateAssociations().stream())
                .map(ContextState::getStateId)
                .collect(Collectors.toSet());

        ControlActionReadDto controlAction = step2Proxy.getControlActionById(contextTable.getControlActionId());

        ComponentReadDto source = controlAction.connection().source();
        ComponentReadDto target = controlAction.connection().target();

        List<VariableReadDto> step2Variables = new ArrayList<>();
        step2Variables.addAll(source.variables());
        step2Variables.addAll(target.variables());

        Set<UUID> step2StatesIds = step2Variables.stream()
                .flatMap(variable -> variable.states().stream())
                .map(StateReadDto::id)
                .collect(Collectors.toSet());

        boolean thereWasChangeInStep2 = !statesIdsOfContextTable.equals(step2StatesIds);

        if (thereWasChangeInStep2) {
            contextTable.getContexts().clear();
            generateAllContexts(step2Variables, 0, new ArrayList<>(), contextTable);
            contextTableRepository.save(contextTable);
        }
    }
}
