package microstamp.step2.unit;

import microstamp.step2.dto.variable.VariableFullReadDto;
import microstamp.step2.dto.variable.VariableInsertDto;
import microstamp.step2.dto.variable.VariableUpdateDto;
import microstamp.step2.entity.Component;
import microstamp.step2.entity.Controller;
import microstamp.step2.entity.Variable;
import microstamp.step2.exception.Step2NotFoundException;
import microstamp.step2.repository.ComponentRepository;
import microstamp.step2.repository.VariableRepository;
import microstamp.step2.service.impl.VariableServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VariableServiceUnitTest {

    @InjectMocks
    private VariableServiceImpl service;

    @Mock
    private VariableRepository variableRepository;

    @Mock
    private ComponentRepository componentRepository;

    @Test
    @DisplayName("#findAll > When no variables are found > Return an empty list")
    void findAllWhenNoVariablesAreFoundReturnAnEmptyList() {
        when(variableRepository.findAll()).thenReturn(List.of());

        assertTrue(service.findAll().isEmpty());
    }

    @Test
    @DisplayName("#findAll > When the variables are found > Return the variables")
    void findAllWhenTheVariablesAreFoundReturnTheVariables() {
        Variable firstVariable = assembleVariable.get();
        Variable secondVarible = assembleVariable.get();

        when(variableRepository.findAll()).thenReturn(List.of(firstVariable, secondVarible));

        List<VariableFullReadDto> response = service.findAll();

        assertAll(
                () -> assertEquals(2, response.size()),
                () -> assertEquals(firstVariable.getId(), response.getFirst().getId()),
                () -> assertEquals(secondVarible.getId(), response.getLast().getId())
        );
    }

    @Test
    @DisplayName("#findById > When no variable is found > Throw an exception")
    void findByIdWhenNoVariableIsFoundThrowAnException() {
        UUID variableUUID = UUID.randomUUID();
        when(variableRepository.findById(variableUUID)).thenReturn(Optional.empty());

        assertThrows(Step2NotFoundException.class, () -> service.findById(variableUUID));
    }

    @Test
    @DisplayName("#findById > When the variable is found > Return it")
    void findByIdWhenTheVariableIsFoundReturnIt() {
        Variable mockVariable = assembleVariable.get();

        when(variableRepository.findById(mockVariable.getId())).thenReturn(Optional.of(mockVariable));

        VariableFullReadDto response = service.findById(mockVariable.getId());
        assertEquals(mockVariable.getId(), response.getId());
    }

    @Test
    @DisplayName("#findByAnalysisId > When no variables are found > Return an empty list")
    void findByAnalysisIdWhenNoVariablesAreFoundReturnAnEmptyList() {
        UUID componentUUID = UUID.randomUUID();
        when(componentRepository.findByAnalysisId(componentUUID)).thenReturn(List.of());
        assertTrue(service.findByAnalysisId(componentUUID).isEmpty());
    }

    @Test
    @DisplayName("#findByAnalysisId > When variables are found > Return it")
    void findByAnalysisIdWhenVariablesAreFoundReturnIt() {
        UUID componentUUID = UUID.randomUUID();
        Variable firstVariable = assembleVariable.get();
        Variable secondVarible = assembleVariable.get();

        Component controller = new Controller();
        controller.setId(componentUUID);
        controller.setVariables(List.of(firstVariable, secondVarible));

        when(componentRepository.findByAnalysisId(componentUUID)).thenReturn(List.of(controller));
        when(variableRepository.findByComponentId(componentUUID)).thenReturn(controller.getVariables());

        List<VariableFullReadDto> response = service.findByAnalysisId(componentUUID);

        assertAll(
                () -> assertEquals(2, response.size()),
                () -> assertEquals(firstVariable.getId(), response.getFirst().getId()),
                () -> assertEquals(secondVarible.getId(), response.getLast().getId())
        );
    }

    @Test
    @DisplayName("#findByComponentId > When no variable is found > Throw an exception")
    void findByComponentIdWhenNoVariableIsFoundThrowAnException() {
        UUID componentUUID = UUID.randomUUID();
        when(variableRepository.findByComponentId(componentUUID)).thenReturn(List.of());
        assertTrue(service.findByComponentId(componentUUID).isEmpty());
    }

    @Test
    @DisplayName("#findByComponentId > When the variable is found > Return it")
    void findByComponentIdWhenTheVariableIsFoundReturnIt() {
        UUID componentUUID = UUID.randomUUID();
        Variable firstVariable = assembleVariable.get();
        Variable secondVarible = assembleVariable.get();

        when(variableRepository.findByComponentId(componentUUID)).thenReturn(List.of(firstVariable, secondVarible));

        List<VariableFullReadDto> response = service.findByComponentId(componentUUID);

        assertAll(
                () -> assertEquals(2, response.size()),
                () -> assertEquals(firstVariable.getId(), response.getFirst().getId()),
                () -> assertEquals(secondVarible.getId(), response.getLast().getId())
        );
    }

    @Test
    @DisplayName("#insert > When the associated component is not found > Throw an exception ")
    void insertWhenTheAssociatedComponentIsNotFoundThrowAnException() {
        VariableInsertDto mockVariableInsert = assembleVariableInsert.get();
        when(componentRepository.findById(mockVariableInsert.getComponentId())).thenReturn(Optional.empty());

        assertThrows(Step2NotFoundException.class, () -> service.insert(mockVariableInsert));
    }

    @Test
    @DisplayName("#insert > When the associated component is found > Insert the variable on database")
    void insertWhenTheAssociatedComponentIsFoundInsertTheVariableOnDatabase() {
        VariableInsertDto mockVariableInsert = assembleVariableInsert.get();
        Component controller = new Controller();
        when(componentRepository.findById(mockVariableInsert.getComponentId())).thenReturn(Optional.of(controller));

        VariableFullReadDto response = service.insert(mockVariableInsert);

        assertAll(
                () -> assertEquals(mockVariableInsert.getName(), response.getName()),
                () -> assertEquals(mockVariableInsert.getCode(), response.getCode()),
                () -> verify(variableRepository, times(1)).save(any())
        );
    }

    @Test
    @DisplayName("#update > When the variable is not found > Throw an exception")
    void updateWhenTheVariableIsNotFoundThrowAnException() {
        UUID variableUUID = UUID.randomUUID();
        when(variableRepository.findById(variableUUID)).thenReturn(Optional.empty());

        assertThrows(Step2NotFoundException.class, () -> service.update(variableUUID, assembleVariableUpdate.get()));
    }

    @Test
    @DisplayName("#update > When the variable is found > Update the variable")
    void updateWhenTheVariableIsFoundUpdateTheVariable() {
        Variable mockVariable = assembleVariable.get();
        VariableUpdateDto variableUpdated = assembleVariableUpdate.get();
        when(variableRepository.findById(mockVariable.getId())).thenReturn(Optional.of(mockVariable));

        service.update(mockVariable.getId(), variableUpdated);

        mockVariable.setName(variableUpdated.getName());
        mockVariable.setCode(variableUpdated.getCode());

        verify(variableRepository, times(1)).save(mockVariable);
    }

    @Test
    @DisplayName("#delete > When the variable is not found > Throw an exception")
    void deleteWhenTheVariableIsNotFoundThrowAnException() {
        UUID variableUUID = UUID.randomUUID();
        when(variableRepository.findById(variableUUID)).thenReturn(Optional.empty());

        assertThrows(Step2NotFoundException.class, () -> service.delete(variableUUID));
    }

    @Test
    @DisplayName("#delete > When the variable is found > Delete it")
    void deleteWhenTheVariableIsFoundDeleteIt() {
        Variable mockVariable = assembleVariable.get();

        when(variableRepository.findById(mockVariable.getId())).thenReturn(Optional.of(mockVariable));

        service.delete(mockVariable.getId());
        verify(variableRepository, times(1)).deleteById(mockVariable.getId());
    }

    private final Supplier<Variable> assembleVariable = () -> Variable.builder()
            .id(UUID.randomUUID())
            .code("Code")
            .name("Name")
            .component(null)
            .states(List.of())
            .build();

    private final Supplier<VariableInsertDto> assembleVariableInsert = () -> VariableInsertDto.builder()
            .name("New Variable Name")
            .code("New Code")
            .componentId(UUID.randomUUID())
            .build();

    private final Supplier<VariableUpdateDto> assembleVariableUpdate = () -> VariableUpdateDto.builder()
            .name("New Variable Name")
            .code("New Code")
            .build();
}
