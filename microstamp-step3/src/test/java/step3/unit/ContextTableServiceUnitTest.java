package step3.unit;

import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import step3.dto.context_table.ContextReadDto;
import step3.dto.context_table.ContextTableCreateDto;
import step3.dto.context_table.ContextTableReadDto;
import step3.dto.context_table.ContextTableReadWithPageDto;
import step3.dto.mapper.ContextTableMapper;
import step3.dto.step2.*;
import step3.entity.Context;
import step3.entity.ContextTable;
import step3.infra.exceptions.OperationNotAllowedException;
import step3.proxy.Step2Proxy;
import step3.repository.ContextRepository;
import step3.repository.ContextTableRepository;
import step3.repository.RuleRepository;
import step3.repository.UnsafeControlActionRepository;
import step3.service.ContextTableService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContextTableServiceUnitTest {
    
    @InjectMocks
    private ContextTableService service;

    @Mock
    private ContextTableRepository contextTableRepository;

    @Mock
    private ContextRepository contextRepository;

    @Mock
    private UnsafeControlActionRepository ucaRepository;

    @Mock
    private RuleRepository ruleRepository;

    @Mock
    private Step2Proxy step2Proxy;

    @Mock
    private ContextTableMapper mapper;

    @Test
    @DisplayName("#createContextTable > When there is an error when communicating with Step 2 proxy > Throw an exception")
    void createContextTableWhenThereIsAnErrorWhenCommunicatingWithStep2ProxyThrowAnException() {
        ContextTableCreateDto mock = assembleContextTableCreate.get();

        when(step2Proxy.getControlActionById(mock.control_action_id())).thenThrow(FeignException.class);

        assertThrows(FeignException.class, () -> service.createContextTable(mock));
    }

    @Test
    @DisplayName("#createContextTable > When the variables are empty > Throw an exception")
    void createContextTableWhenTheVariablesAreEmptyThrowAnException() {
        ContextTableCreateDto mock = assembleContextTableCreate.get();
        ControlActionReadDto mockControlActionRead = assembleControlActionReadWithoutVariables.apply(mock.control_action_id());

        when(step2Proxy.getControlActionById(mock.control_action_id())).thenReturn(mockControlActionRead);

        assertThrows(OperationNotAllowedException.class, () -> service.createContextTable(mock));
    }

    @Test
    @DisplayName("#createContextTable > When the variables are not empty > When there is an empty state > Throw an exception")
    void createContextTableWhenTheVariablesAreNotEmptyWhenThereIsAnEmptyStateThrowAnException() {
        ContextTableCreateDto mock = assembleContextTableCreate.get();
        ControlActionReadDto mockControlActionRead = assembleControlActionReadWithVariablesButEmptyState.apply(mock.control_action_id());

        when(step2Proxy.getControlActionById(mock.control_action_id())).thenReturn(mockControlActionRead);

        assertThrows(OperationNotAllowedException.class, () -> service.createContextTable(mock));
    }

    @Test
    @DisplayName("#createContextTable > When there is a context table for the given control action > Throw an exception")
    void createContextTableWhenThereIsAContextTableForTheGivenControlActionThrowAnException() {
        ContextTableCreateDto mock = assembleContextTableCreate.get();
        ControlActionReadDto mockControlActionRead = assembleControlActionRead.apply(mock.control_action_id());

        when(step2Proxy.getControlActionById(mock.control_action_id())).thenReturn(mockControlActionRead);
        when(contextTableRepository.findByControlActionId(mock.control_action_id())).thenReturn(Optional.of(assembleContextTable.apply(UUID.randomUUID())));

        assertAll(
                () -> assertThrows(OperationNotAllowedException.class, () -> service.createContextTable(mock)),
                () -> verify(contextTableRepository, times(1)).findByControlActionId(mock.control_action_id()),
                () -> verify(contextTableRepository, times(0)).save(any())
        );
    }

    @Test
    @DisplayName("#createContextTable > When all information are valid > Create the context table")
    void createContextTableWhenAllInformationAreValidCreateTheContextTable() {
        ContextTableCreateDto mock = assembleContextTableCreate.get();
        ControlActionReadDto mockControlActionRead = assembleControlActionRead.apply(mock.control_action_id());

        when(step2Proxy.getControlActionById(mock.control_action_id())).thenReturn(mockControlActionRead);
        when(contextTableRepository.findByControlActionId(mock.control_action_id())).thenReturn(Optional.empty());
        when(contextTableRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(mapper.toContextTableReadDto(any())).thenAnswer(invocation -> {
            ContextTable contextTable = invocation.getArgument(0);
            return ContextTableReadDto.builder()
                    .id(contextTable.getId())
                    .control_action_id(contextTable.getControlActionId())
                    .contexts(contextTable.getContexts().stream().map(ct -> ContextReadDto.builder().id(ct.getId()).build()).toList())
                    .build();
        });

        ContextTableReadDto response = service.createContextTable(mock);

        assertAll(
                () -> assertEquals(4, response.contexts().size()),
                () -> verify(contextTableRepository, times(1)).findByControlActionId(mock.control_action_id()),
                () -> verify(contextTableRepository, times(1)).save(any())
        );
    }


    @Test
    @DisplayName("#readContextTableById > When no context table is found > Throw an exception")
    void readContextTableByIdWhenNoContextTableIsFoundThrowAnException() {
        UUID contextTableId = UUID.randomUUID();

        when(contextTableRepository.findById(contextTableId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.readContextTableById(contextTableId, 0, 10));
    }

    @Test
    @DisplayName("#readContextTableById > When the context table is found > Return it")
    void readContextTableByIdWhenTheContextTableIsFoundReturnIt() {
        UUID contextTableId = UUID.randomUUID();
        ContextTable mockContextTable = assembleContextTable.apply(contextTableId);
        ControlActionReadDto mockControlActionRead = assembleControlActionRead.apply(mockContextTable.getControlActionId());

        when(step2Proxy.getControlActionById(mockContextTable.getControlActionId())).thenReturn(mockControlActionRead);
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        Page<Context> mockContext = new PageImpl<>(List.of(assembleContext.apply(mockContextTable.getId())), pageable, 0);

        when(contextTableRepository.findById(contextTableId)).thenReturn(Optional.of(mockContextTable));
        when(contextRepository.findByContextTableId(mockContextTable.getId(), pageable)).thenReturn(mockContext);
        when(mapper.toContextTableReadWithPageDto(mockContextTable, mockContext)).thenAnswer(invocation -> {
            ContextTable contextTable = invocation.getArgument(0);
            Page<Context> context = invocation.getArgument(1);

            return ContextTableReadWithPageDto.builder()
                    .id(contextTable.getId())
                    .totalPages(context.getTotalPages())
                    .currentPage(context.getNumber())
                    .build();
        });

        ContextTableReadWithPageDto response = service.readContextTableById(contextTableId, 0, 10);

        assertAll(
                () -> assertEquals(mockContextTable.getId(), response.id())
        );
    }

    @Test
    @DisplayName("#readAllContextTables > When no context table is found > Return an empty list")
    void readAllContextTablesWhenNoContextTableIsFoundReturnAnEmptyList() {
        when(contextTableRepository.findAll()).thenReturn(new ArrayList<>());

        assertTrue(service.readAllContextTables().isEmpty());
    }

    @Test
    @DisplayName("#readAllContextTables > When the context table is found > Return the context table")
    void readAllContextTablesWhenTheContextTableIsFoundReturnTheContextTable() {
        ContextTable mockContextTable = assembleContextTable.apply(UUID.randomUUID());

        when(contextTableRepository.findAll()).thenReturn(List.of(mockContextTable));
        when(mapper.toContextTableReadDto(any())).thenAnswer(invocation -> {
            ContextTable contextTable = invocation.getArgument(0);
            return ContextTableReadDto.builder()
                    .id(contextTable.getId())
                    .control_action_id(contextTable.getControlActionId())
                    .build();
        });

        assertFalse(service.readAllContextTables().isEmpty());
    }

    @Test
    @DisplayName("#readContextTableByControlActionId > When there is an error when communicating with Step 2 proxy > Throw an exception")
    void readContextTableByControlActionIdWhenThereIsAnErrorWhenCommunicatingWithStep2ProxyThrowAnException() {
        UUID controlActionId = UUID.randomUUID();
        when(step2Proxy.getControlActionById(controlActionId)).thenThrow(FeignException.class);

        assertThrows(FeignException.class, () -> service.readContextTableByControlActionId(controlActionId, 0, 10));
    }

    @Test
    @DisplayName("#readContextTableByControlActionId > When no context table is found > Throw an exception")
    void readContextTableByControlActionIdWhenNoContextTableIsFoundThrowAnException() {
        UUID mockControlActionId = UUID.randomUUID();
        when(step2Proxy.getControlActionById(mockControlActionId)).thenReturn(null);
        when(contextTableRepository.findByControlActionId(mockControlActionId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.readContextTableByControlActionId(mockControlActionId, 0, 10));
    }

    @Test
    @DisplayName("#readContextTableByControlActionId > When the context table is found > Return it")
    void readContextTableByControlActionIdWhenTheContextTableIsFoundReturnIt() {
//        UUID mockControlActionId = UUID.randomUUID();
        UUID mockContextTableId = UUID.randomUUID();
        ContextTable mockContextTable = assembleContextTable.apply(mockContextTableId);
        ControlActionReadDto mockControlActionRead = assembleControlActionRead.apply(mockContextTable.getControlActionId());

        Pageable pageable = Pageable.ofSize(10).withPage(0);
        Page<Context> mockContext = new PageImpl<>(List.of(assembleContext.apply(mockContextTable.getId())), pageable, 0);


        when(step2Proxy.getControlActionById(mockContextTable.getControlActionId())).thenReturn(mockControlActionRead);
        when(contextTableRepository.findByControlActionId(mockContextTable.getControlActionId())).thenReturn(Optional.of(mockContextTable));
        when(contextRepository.findByContextTableId(mockContextTable.getId(), pageable)).thenReturn(mockContext);
        when(mapper.toContextTableReadWithPageDto(mockContextTable, mockContext)).thenAnswer(invocation -> {
            ContextTable contextTable = invocation.getArgument(0);
            Page<Context> context = invocation.getArgument(1);

            return ContextTableReadWithPageDto.builder()
                    .id(contextTable.getId())
                    .totalPages(context.getTotalPages())
                    .currentPage(context.getNumber())
                    .build();
        });

        ContextTableReadWithPageDto response = service.readContextTableByControlActionId(mockContextTable.getControlActionId(), 0, 10);

        assertAll(
                () -> assertEquals(mockContextTable.getId(), response.id())
        );
    }

    @Test
    @DisplayName("#deleteContextTable > When no context table are found > Throw an exception")
    void deleteContextTableWhenNoContextTableAreFoundThrowAnException() {
        UUID mockContextTableId = UUID.randomUUID();

        when(contextTableRepository.findById(mockContextTableId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.deleteContextTable(mockContextTableId));
    }

    @Test
    @DisplayName("#deleteContextTable > When the context table is found > Delete it and the related UCAs")
    void deleteContextTableWhenTheContextTableIsFoundDeleteItAndTheRelatedUcAs() {
        ContextTable mock = assembleContextTable.apply(UUID.randomUUID());

        when(contextTableRepository.findById(mock.getId())).thenReturn(Optional.of(mock));

        service.deleteContextTable(mock.getId());

        assertAll(
                () -> verify(ucaRepository, times(1)).deleteByControlActionId(mock.getControlActionId()),
                () -> verify(contextTableRepository, times(1)).deleteById(mock.getId())
        );
    }

    @Test
    @DisplayName("#generateContextTable > When there is no variables associated > Generate a context table with one context")
    void generateContextTableWhenThereIsNoVariablesAssociatedGenerateAContextTableWithOneContext() {
        ContextTable response = service.generateContextTable(List.of());

        assertEquals(1, response.getContexts().size());
    }

    @Test
    @DisplayName("#generateContextTable > When there are variables associated > Generate the context table")
    void generateContextTableWhenThereAreVariablesAssociatedGenerateTheContextTable() {
        VariableReadDto mockFirst = VariableReadDto.builder()
                .id(UUID.randomUUID())
                .name("Variable 1")
                .code("V1")
                .states(List.of(StateReadDto.builder()
                        .id(UUID.randomUUID())
                        .name("State 1")
                        .code("S1")
                        .build(), StateReadDto.builder()
                        .id(UUID.randomUUID())
                        .name("State 2")
                        .code("S2")
                        .build()))
                .build();

        VariableReadDto mockSecond = VariableReadDto.builder()
                .id(UUID.randomUUID())
                .name("Variable 2")
                .code("V1")
                .states(List.of(StateReadDto.builder()
                        .id(UUID.randomUUID())
                        .name("State 3")
                        .code("S3")
                        .build(), StateReadDto.builder()
                        .id(UUID.randomUUID())
                        .name("State 4")
                        .code("S4")
                        .build()))
                .build();

        ContextTable response = service.generateContextTable(List.of(mockFirst, mockSecond));

        assertEquals(4, response.getContexts().size());
    }

    private final Function<UUID, ContextTable> assembleContextTable = (id) -> ContextTable.builder()
            .id(id)
            .controlActionId(UUID.randomUUID())
            .contexts(List.of())
            .build();

    private final Function<UUID, ControlActionReadDto> assembleControlActionRead = (id) -> ControlActionReadDto.builder()
            .id(id)
            .connection(ConnectionReadDto.builder()
                    .source(ComponentReadDto.builder().id(UUID.randomUUID())
                            .variables(List.of(VariableReadDto.builder()
                                    .id(UUID.randomUUID())
                                    .name("Variable 1")
                                    .code("V1")
                                    .states(List.of(StateReadDto.builder()
                                            .id(UUID.randomUUID())
                                            .name("State 1")
                                            .code("S1")
                                            .build(), StateReadDto.builder()
                                            .id(UUID.randomUUID())
                                            .name("State 2")
                                            .code("S2")
                                            .build()))
                                    .build()))
                            .build())
                    .target(ComponentReadDto.builder().id(UUID.randomUUID())
                            .variables(List.of(VariableReadDto.builder()
                                    .id(UUID.randomUUID())
                                    .name("Variable 2")
                                    .code("V2")
                                    .states(List.of(StateReadDto.builder()
                                            .id(UUID.randomUUID())
                                            .name("State 3")
                                            .code("S3")
                                            .build(), StateReadDto.builder()
                                            .id(UUID.randomUUID())
                                            .name("State 3")
                                            .code("S3")
                                            .build()))
                                    .build()))
                            .build())
                    .build())
            .build();

    private final Function<UUID, ControlActionReadDto> assembleControlActionReadWithoutVariables = (id) -> ControlActionReadDto.builder()
            .id(id)
            .connection(ConnectionReadDto.builder()
                    .source(ComponentReadDto.builder().id(UUID.randomUUID()).variables(List.of()).build())
                    .target(ComponentReadDto.builder().id(UUID.randomUUID()).variables(List.of()).build())
                    .build())
            .build();

    private final Function<UUID, ControlActionReadDto> assembleControlActionReadWithVariablesButEmptyState = (id) -> ControlActionReadDto.builder()
            .id(id)
            .connection(ConnectionReadDto.builder()
                    .source(ComponentReadDto.builder().id(UUID.randomUUID()).variables(List.of(VariableReadDto.builder().states(List.of()).build())).build())
                    .target(ComponentReadDto.builder().id(UUID.randomUUID()).variables(List.of()).build())
                    .build())
            .build();

    private final Function<UUID, Context> assembleContext = (contextTableId) -> Context.builder()
            .id(UUID.randomUUID())
            .stateAssociations(List.of())
            .build();

    private final Supplier<ContextTableCreateDto> assembleContextTableCreate = () -> ContextTableCreateDto.builder()
            .control_action_id(UUID.randomUUID())
            .build();

}
