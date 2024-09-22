package microstamp.step2.unit;

import microstamp.step2.dto.state.StateInsertDto;
import microstamp.step2.dto.state.StateReadDto;
import microstamp.step2.dto.state.StateUpdateDto;
import microstamp.step2.entity.State;
import microstamp.step2.entity.Variable;
import microstamp.step2.exception.Step2NotFoundException;
import microstamp.step2.repository.StateRepository;
import microstamp.step2.repository.VariableRepository;
import microstamp.step2.service.impl.StateServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StateServiceUnitTest {

    @InjectMocks
    private StateServiceImpl service;

    @Mock
    private StateRepository stateRepository;

    @Mock
    private VariableRepository variableRepository;

    @Test
    @DisplayName("#findAll > When no state are found > Return an empty list")
    void findAllWhenNoStateAreFoundReturnAnEmptyList() {
        when(stateRepository.findAll()).thenReturn(List.of());

        assertTrue(service.findAll().isEmpty());
    }

    @Test
    @DisplayName("#findAll > When the states are found > Return the states")
    void findAllWhenTheStatesAreFoundReturnTheStates() {
        State firstState = assembleState.get();
        State secondState = assembleState.get();

        when(stateRepository.findAll()).thenReturn(List.of(firstState, secondState));

        List<StateReadDto> response = service.findAll();

        assertAll(
                () -> assertEquals(2, response.size()),
                () -> assertEquals(firstState.getId(), response.getFirst().getId()),
                () -> assertEquals(secondState.getId(), response.getLast().getId())
        );
    }

    @Test
    @DisplayName("#findById > When no state is found > Throw an exception")
    void findByIdWhenNoStateIsFoundThrowAnException() {
        UUID stateUUID = UUID.randomUUID();
        when(stateRepository.findById(stateUUID)).thenReturn(Optional.empty());

        assertThrows(Step2NotFoundException.class, () -> service.findById(stateUUID));
    }

    @Test
    @DisplayName("#findById > When the state is found > Return it")
    void findByIdWhenTheStateIsFoundReturnIt() {
        State mockState = assembleState.get();

        when(stateRepository.findById(mockState.getId())).thenReturn(Optional.of(mockState));

        StateReadDto response = service.findById(mockState.getId());
        assertEquals(mockState.getId(), response.getId());
    }

    @Test
    @DisplayName("#insert > When the associated variables is not found > Throw an exception ")
    void insertWhenTheAssociatedVariablesIsNotFoundThrowAnException() {
        StateInsertDto mockStateInsert = assembleStateInsert.get();
        when(variableRepository.findById(mockStateInsert.getVariableId())).thenReturn(Optional.empty());

        assertThrows(Step2NotFoundException.class, () -> service.insert(mockStateInsert));
    }

    @Test
    @DisplayName("#insert > When the associated variable is found > Insert the state on database")
    void insertWhenTheAssociatedVariableIsFoundInsertTheStateOnDatabase() {
        StateInsertDto mockStateInsert = assembleStateInsert.get();

        when(variableRepository.findById(mockStateInsert.getVariableId())).thenReturn(Optional.of(assembleVariable.get()));

        StateReadDto response = service.insert(mockStateInsert);

        assertAll(
                () -> assertEquals(mockStateInsert.getName(), response.getName()),
                () -> assertEquals(mockStateInsert.getCode(), response.getCode()),
                () -> verify(stateRepository, times(1)).save(any())
        );
    }


    @Test
    @DisplayName("#update > When the state is not found > Throw an exception")
    void updateWhenTheStateIsNotFoundThrowAnException() {
        UUID stateUUID = UUID.randomUUID();
        when(stateRepository.findById(stateUUID)).thenReturn(Optional.empty());

        assertThrows(Step2NotFoundException.class, () -> service.update(stateUUID, assembleStateUpdate.get()));
    }

    @Test
    @DisplayName("#update > When the state is found > Update the state")
    void updateWhenTheStateIsFoundUpdateTheState() {
        State mockState = assembleState.get();
        StateUpdateDto stateUpdated = assembleStateUpdate.get();
        when(stateRepository.findById(mockState.getId())).thenReturn(Optional.of(mockState));

        service.update(mockState.getId(), stateUpdated);

        mockState.setName(stateUpdated.getName());
        mockState.setCode(stateUpdated.getCode());

        verify(stateRepository, times(1)).save(mockState);
    }


    @Test
    @DisplayName("#delete > When the state is not found > Throw an exception")
    void deleteWhenTheStateIsNotFoundThrowAnException() {
        UUID stateUUID = UUID.randomUUID();
        when(stateRepository.findById(stateUUID)).thenReturn(Optional.empty());

        assertThrows(Step2NotFoundException.class, () -> service.delete(stateUUID));
    }

    @Test
    @DisplayName("#delete > When the state is found > Delete it")
    void deleteWhenTheStateIsFoundDeleteIt() {
        State mockState = assembleState.get();

        when(stateRepository.findById(mockState.getId())).thenReturn(Optional.of(mockState));

        service.delete(mockState.getId());
        verify(stateRepository, times(1)).deleteById(mockState.getId());
    }


    private final Supplier<State> assembleState = () -> State.builder()
            .id(UUID.randomUUID())
            .code("Code")
            .name("Name")
            .variable(Variable.builder().build())
            .build();

    private final Supplier<StateInsertDto> assembleStateInsert = () -> StateInsertDto.builder()
            .name("New State")
            .code("New Code")
            .variableId(UUID.randomUUID())
            .build();

    private final Supplier<StateUpdateDto> assembleStateUpdate = () -> StateUpdateDto.builder()
            .name("Updated name")
            .code("Updated code")
            .build();

    private final Supplier<Variable> assembleVariable = () -> Variable.builder()
            .id(UUID.randomUUID())
            .name("Variable")
            .code("Code")
            .states(new ArrayList<>())
            .build();


}
