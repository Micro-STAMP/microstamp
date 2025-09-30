package microstamp.step4new.unit;

import microstamp.step4new.dto.highlevelsolution.HighLevelSolutionInsertDto;
import microstamp.step4new.dto.highlevelsolution.HighLevelSolutionReadDto;
import microstamp.step4new.dto.highlevelsolution.HighLevelSolutionUpdateDto;
import microstamp.step4new.entity.FormalScenario;
import microstamp.step4new.entity.FormalScenarioClass;
import microstamp.step4new.entity.HighLevelSolution;
import microstamp.step4new.exception.Step4NewIllegalArgumentException;
import microstamp.step4new.exception.Step4NewNotFoundException;
import microstamp.step4new.repository.FormalScenarioClassRepository;
import microstamp.step4new.repository.HighLevelSolutionRepository;
import microstamp.step4new.service.impl.HighLevelSolutionServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HighLevelSolutionServiceUnitTest {

    @InjectMocks
    private HighLevelSolutionServiceImpl service;

    @Mock
    private HighLevelSolutionRepository repository;

    @Mock
    private FormalScenarioClassRepository formalScenarioClassRepository;

    @Test
    @DisplayName("#findById > When HighLevelSolution is not found > Throw an exception")
    void findByIdWhenHighLevelSolutionIsNotFoundThrowAnException() {
        UUID id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(Step4NewNotFoundException.class, () -> service.findById(id));
        
        verify(repository, times(1)).findById(id);
    }

    @Test
    @DisplayName("#findById > When HighLevelSolution is found > Return mapped DTO")
    void findByIdWhenHighLevelSolutionIsFoundReturnMappedDto() {
        UUID id = UUID.randomUUID();
        HighLevelSolution solution = assembleHighLevelSolution.apply(id);

        when(repository.findById(id)).thenReturn(Optional.of(solution));

        HighLevelSolutionReadDto response = service.findById(id);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(solution.getId(), response.getId())
        );
        
        verify(repository, times(1)).findById(id);
    }

    @Test
    @DisplayName("#getOrCreateByFormalScenarioClassId > When HighLevelSolution exists > Return existing")
    void getOrCreateByFormalScenarioClassIdWhenHighLevelSolutionExistsReturnExisting() {
        UUID classId = UUID.randomUUID();
        HighLevelSolution existingSolution = assembleHighLevelSolution.apply(UUID.randomUUID());

        when(repository.findByFormalScenarioClassId(classId)).thenReturn(Optional.of(existingSolution));

        HighLevelSolutionReadDto response = service.getOrCreateByFormalScenarioClassId(classId);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(existingSolution.getId(), response.getId())
        );
        
        verify(repository, times(1)).findByFormalScenarioClassId(classId);
        verify(formalScenarioClassRepository, never()).findById(any());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("#create > When FormalScenarioClass is not found > Throw an exception")
    void createWhenFormalScenarioClassIsNotFoundThrowAnException() {
        HighLevelSolutionInsertDto insertDto = assembleHighLevelSolutionInsertDto.get();

        when(formalScenarioClassRepository.findById(insertDto.getFormalScenarioClassId())).thenReturn(Optional.empty());

        assertThrows(Step4NewNotFoundException.class, () -> service.create(insertDto));
        
        verify(formalScenarioClassRepository, times(1)).findById(insertDto.getFormalScenarioClassId());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("#create > When HighLevelSolution already exists for class > Throw an exception")
    void createWhenHighLevelSolutionAlreadyExistsForClassThrowAnException() {
        HighLevelSolutionInsertDto insertDto = assembleHighLevelSolutionInsertDto.get();
        FormalScenarioClass formalClass = assembleFormalScenarioClass.apply(insertDto.getFormalScenarioClassId());
        HighLevelSolution existingSolution = assembleHighLevelSolution.apply(UUID.randomUUID());

        when(formalScenarioClassRepository.findById(insertDto.getFormalScenarioClassId())).thenReturn(Optional.of(formalClass));
        when(repository.findByFormalScenarioClassId(formalClass.getId())).thenReturn(Optional.of(existingSolution));

        assertThrows(Step4NewIllegalArgumentException.class, () -> service.create(insertDto));
        
        verify(formalScenarioClassRepository, times(1)).findById(insertDto.getFormalScenarioClassId());
        verify(repository, times(1)).findByFormalScenarioClassId(formalClass.getId());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("#create > When all inputs are valid > Create HighLevelSolution")
    void createWhenAllInputsAreValidCreateHighLevelSolution() {
        HighLevelSolutionInsertDto insertDto = assembleHighLevelSolutionInsertDto.get();
        FormalScenarioClass formalClass = assembleFormalScenarioClass.apply(insertDto.getFormalScenarioClassId());

        when(formalScenarioClassRepository.findById(insertDto.getFormalScenarioClassId())).thenReturn(Optional.of(formalClass));
        when(repository.findByFormalScenarioClassId(formalClass.getId())).thenReturn(Optional.empty());
        when(repository.save(any(HighLevelSolution.class))).thenAnswer(invocation -> {
            HighLevelSolution hls = invocation.getArgument(0);
            if (hls.getId() == null) hls.setId(UUID.randomUUID());
            return hls;
        });

        HighLevelSolutionReadDto response = service.create(insertDto);

        assertNotNull(response);

        verify(formalScenarioClassRepository, times(1)).findById(insertDto.getFormalScenarioClassId());
        verify(repository, times(1)).findByFormalScenarioClassId(formalClass.getId());
        verify(repository, times(1)).save(any(HighLevelSolution.class));
    }

    @Test
    @DisplayName("#update > When HighLevelSolution is not found > Throw an exception")
    void updateWhenHighLevelSolutionIsNotFoundThrowAnException() {
        UUID id = UUID.randomUUID();
        HighLevelSolutionUpdateDto updateDto = assembleHighLevelSolutionUpdateDto.get();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(Step4NewNotFoundException.class, () -> service.update(id, updateDto));
        
        verify(repository, times(1)).findById(id);
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("#update > When HighLevelSolution is found > Update the HighLevelSolution")
    void updateWhenHighLevelSolutionIsFoundUpdateTheHighLevelSolution() {
        UUID id = UUID.randomUUID();
        HighLevelSolution existingSolution = assembleHighLevelSolution.apply(id);
        HighLevelSolutionUpdateDto updateDto = assembleHighLevelSolutionUpdateDto.get();

        when(repository.findById(id)).thenReturn(Optional.of(existingSolution));

        assertDoesNotThrow(() -> service.update(id, updateDto));

        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(existingSolution);
    }

    @Test
    @DisplayName("#delete > When called with valid id > Delete the HighLevelSolution")
    void deleteWhenCalledWithValidIdDeleteTheHighLevelSolution() {
        UUID id = UUID.randomUUID();

        service.delete(id);

        verify(repository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("#getOrCreateByUnsafeControlActionId > When no FormalScenarioClasses are found > Return empty list")
    void getOrCreateByUnsafeControlActionIdWhenNoFormalScenarioClassesAreFoundReturnEmptyList() {
        UUID ucaId = UUID.randomUUID();

        when(formalScenarioClassRepository.findByFormalScenario_UnsafeControlActionId(ucaId)).thenReturn(List.of());

        List<HighLevelSolutionReadDto> response = service.getOrCreateByUnsafeControlActionId(ucaId);

        assertTrue(response.isEmpty());
        verify(formalScenarioClassRepository, times(1)).findByFormalScenario_UnsafeControlActionId(ucaId);
    }

    @Test
    @DisplayName("#getOrCreateByUnsafeControlActionId > When FormalScenarioClasses are found > Return solutions")
    void getOrCreateByUnsafeControlActionIdWhenFormalScenarioClassesAreFoundReturnSolutions() {
        UUID ucaId = UUID.randomUUID();
        FormalScenarioClass formalClass = assembleFormalScenarioClass.apply(UUID.randomUUID());
        HighLevelSolution existingSolution = assembleHighLevelSolution.apply(UUID.randomUUID());
        existingSolution.setFormalScenarioClass(formalClass);

        when(formalScenarioClassRepository.findByFormalScenario_UnsafeControlActionId(ucaId)).thenReturn(List.of(formalClass));
        when(repository.findByFormalScenarioClass_FormalScenario_UnsafeControlActionId(ucaId)).thenReturn(List.of(existingSolution));

        List<HighLevelSolutionReadDto> response = service.getOrCreateByUnsafeControlActionId(ucaId);

        assertEquals(1, response.size());
        verify(formalScenarioClassRepository, times(1)).findByFormalScenario_UnsafeControlActionId(ucaId);
        verify(repository, times(1)).findByFormalScenarioClass_FormalScenario_UnsafeControlActionId(ucaId);
    }

    private final Function<UUID, FormalScenarioClass> assembleFormalScenarioClass = (id) -> {
        FormalScenario formalScenario = FormalScenario.builder()
                .id(UUID.randomUUID())
                .analysisId(UUID.randomUUID())
                .unsafeControlActionId(UUID.randomUUID())
                .build();
        
        return FormalScenarioClass.builder()
                .id(id)
                .code("CLASS1")
                .formalScenario(formalScenario)
                .build();
    };

    private final Function<UUID, HighLevelSolution> assembleHighLevelSolution = (id) -> HighLevelSolution.builder()
            .id(id)
            .processBehavior("Test process behavior")
            .controllerBehavior("Test controller behavior")
            .otherSolutions("Test other solutions")
            .formalScenarioClass(assembleFormalScenarioClass.apply(UUID.randomUUID()))
            .build();

    private final Supplier<HighLevelSolutionInsertDto> assembleHighLevelSolutionInsertDto = () -> HighLevelSolutionInsertDto.builder()
            .formalScenarioClassId(UUID.randomUUID())
            .processBehavior("Test process behavior")
            .controllerBehavior("Test controller behavior")
            .otherSolutions("Test other solutions")
            .build();

    private final Supplier<HighLevelSolutionUpdateDto> assembleHighLevelSolutionUpdateDto = () -> HighLevelSolutionUpdateDto.builder()
            .processBehavior("Updated process behavior")
            .controllerBehavior("Updated controller behavior")
            .otherSolutions("Updated other solutions")
            .build();
}