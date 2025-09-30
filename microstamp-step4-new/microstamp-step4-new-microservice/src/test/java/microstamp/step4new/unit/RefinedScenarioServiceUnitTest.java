package microstamp.step4new.unit;

import microstamp.step4new.dto.refinedscenario.*;
import microstamp.step4new.entity.FormalScenario;
import microstamp.step4new.entity.FormalScenarioClass;
import microstamp.step4new.entity.RefinedScenario;
import microstamp.step4new.entity.RefinedScenarioCommonCause;
import microstamp.step4new.exception.Step4NewNotFoundException;
import microstamp.step4new.repository.FormalScenarioClassRepository;
import microstamp.step4new.repository.RefinedScenarioCommonCauseRepository;
import microstamp.step4new.repository.RefinedScenarioRepository;
import microstamp.step4new.repository.RefinedScenarioTemplateRepository;
import microstamp.step4new.service.impl.RefinedScenarioServiceImpl;
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
public class RefinedScenarioServiceUnitTest {

    @InjectMocks
    private RefinedScenarioServiceImpl service;

    @Mock
    private RefinedScenarioCommonCauseRepository commonCauseRepository;

    @Mock
    private RefinedScenarioRepository refinedScenarioRepository;

    @Mock
    private RefinedScenarioTemplateRepository templateRepository;

    @Mock
    private FormalScenarioClassRepository formalScenarioClassRepository;

    @Test
    @DisplayName("#findByUnsafeControlActionId > When no RefinedScenarios are found > Return empty list")
    void findByUnsafeControlActionIdWhenNoRefinedScenariosAreFoundReturnEmptyList() {
        UUID ucaId = UUID.randomUUID();

        when(refinedScenarioRepository.findByUnsafeControlActionId(ucaId)).thenReturn(List.of());

        List<RefinedScenarioReadDto> response = service.findByUnsafeControlActionId(ucaId);

        assertTrue(response.isEmpty());
        verify(refinedScenarioRepository, times(1)).findByUnsafeControlActionId(ucaId);
    }

    @Test
    @DisplayName("#findByUnsafeControlActionId > When RefinedScenarios are found > Return mapped DTOs")
    void findByUnsafeControlActionIdWhenRefinedScenariosAreFoundReturnMappedDtos() {
        UUID ucaId = UUID.randomUUID();
        RefinedScenario scenario1 = assembleRefinedScenario.get();
        RefinedScenario scenario2 = assembleRefinedScenario.get();

        when(refinedScenarioRepository.findByUnsafeControlActionId(ucaId)).thenReturn(List.of(scenario1, scenario2));

        List<RefinedScenarioReadDto> response = service.findByUnsafeControlActionId(ucaId);

        assertAll(
                () -> assertEquals(2, response.size()),
                () -> assertEquals(scenario1.getId(), response.getFirst().getId()),
                () -> assertEquals(scenario2.getId(), response.get(1).getId())
        );
        
        verify(refinedScenarioRepository, times(1)).findByUnsafeControlActionId(ucaId);
    }

    @Test
    @DisplayName("#groupByCommonCauseForUnsafeControlAction > When RefinedScenarios are found > Group them by common cause")
    void groupByCommonCauseForUnsafeControlActionWhenRefinedScenariosAreFoundGroupThemByCommonCause() {
        UUID ucaId = UUID.randomUUID();
        RefinedScenarioCommonCause commonCause = assembleCommonCause.apply("CC-1");
        RefinedScenario scenario1 = assembleRefinedScenario.get();
        scenario1.setCommonCause(commonCause);
        RefinedScenario scenario2 = assembleRefinedScenario.get();
        scenario2.setCommonCause(commonCause);

        when(refinedScenarioRepository.findByUnsafeControlActionId(ucaId)).thenReturn(List.of(scenario1, scenario2));
        when(templateRepository.findByCommonCause_Id(commonCause.getId())).thenReturn(List.of());

        List<RefinedScenarioCommonCauseGroupReadDto> response = service.groupByCommonCauseForUnsafeControlAction(ucaId);

        assertAll(
                () -> assertEquals(1, response.size()),
                () -> assertEquals(commonCause.getCode(), response.getFirst().getCode()),
                () -> assertEquals(2, response.getFirst().getRefinedScenarios().size())
        );
        
        verify(refinedScenarioRepository, times(1)).findByUnsafeControlActionId(ucaId);
        verify(templateRepository, times(1)).findByCommonCause_Id(commonCause.getId());
    }

    @Test
    @DisplayName("#insert > When RefinedScenarioCommonCause is not found > Throw an exception")
    void insertWhenRefinedScenarioCommonCauseIsNotFoundThrowAnException() {
        UUID commonCauseId = UUID.randomUUID();
        RefinedScenarioInsertDto insertDto = assembleRefinedScenarioInsertDto.apply(commonCauseId);

        when(commonCauseRepository.findById(commonCauseId)).thenReturn(Optional.empty());

        assertThrows(Step4NewNotFoundException.class, () -> service.insert(insertDto));
        
        verify(commonCauseRepository, times(1)).findById(commonCauseId);
        verify(formalScenarioClassRepository, never()).findById(any());
        verify(refinedScenarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("#insert > When FormalScenarioClass is not found > Throw an exception")
    void insertWhenFormalScenarioClassIsNotFoundThrowAnException() {
        UUID commonCauseId = UUID.randomUUID();
        UUID formalClassId = UUID.randomUUID();
        RefinedScenarioInsertDto insertDto = assembleRefinedScenarioInsertDto.apply(commonCauseId);
        insertDto.setFormalScenarioClassId(formalClassId);
        RefinedScenarioCommonCause commonCause = assembleCommonCause.apply("CC-1");

        when(commonCauseRepository.findById(commonCauseId)).thenReturn(Optional.of(commonCause));
        when(formalScenarioClassRepository.findById(formalClassId)).thenReturn(Optional.empty());

        assertThrows(Step4NewNotFoundException.class, () -> service.insert(insertDto));
        
        verify(commonCauseRepository, times(1)).findById(commonCauseId);
        verify(formalScenarioClassRepository, times(1)).findById(formalClassId);
        verify(refinedScenarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("#insert > When all inputs are valid > Create RefinedScenario with generated code")
    void insertWhenAllInputsAreValidCreateRefinedScenarioWithGeneratedCode() {
        UUID commonCauseId = UUID.randomUUID();
        UUID formalClassId = UUID.randomUUID();
        RefinedScenarioInsertDto insertDto = assembleRefinedScenarioInsertDto.apply(commonCauseId);
        insertDto.setFormalScenarioClassId(formalClassId);
        RefinedScenarioCommonCause commonCause = assembleCommonCause.apply("CC-1");
        FormalScenarioClass formalClass = assembleFormalScenarioClass.apply(formalClassId);

        when(commonCauseRepository.findById(commonCauseId)).thenReturn(Optional.of(commonCause));
        when(formalScenarioClassRepository.findById(formalClassId)).thenReturn(Optional.of(formalClass));
        when(refinedScenarioRepository.findMaxCodeNumberByAnalysisId(any(UUID.class))).thenReturn(3);
        when(refinedScenarioRepository.save(any(RefinedScenario.class))).thenAnswer(invocation -> {
            RefinedScenario rs = invocation.getArgument(0);
            if (rs.getId() == null) rs.setId(UUID.randomUUID());
            return rs;
        });

        RefinedScenarioReadDto response = service.insert(insertDto);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals("RSC-4", response.getCode()),
                () -> assertEquals(insertDto.getRefinedScenario(), response.getRefinedScenario())
        );

        verify(commonCauseRepository, times(1)).findById(commonCauseId);
        verify(formalScenarioClassRepository, times(1)).findById(formalClassId);
        verify(refinedScenarioRepository, times(1)).save(any(RefinedScenario.class));
    }

    @Test
    @DisplayName("#update > When RefinedScenario is not found > Throw an exception")
    void updateWhenRefinedScenarioIsNotFoundThrowAnException() {
        UUID id = UUID.randomUUID();
        RefinedScenarioUpdateDto updateDto = assembleRefinedScenarioUpdateDto.get();

        when(refinedScenarioRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(Step4NewNotFoundException.class, () -> service.update(id, updateDto));
        
        verify(refinedScenarioRepository, times(1)).findById(id);
        verify(commonCauseRepository, never()).findById(any());
        verify(refinedScenarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("#update > When RefinedScenarioCommonCause is not found > Throw an exception")
    void updateWhenRefinedScenarioCommonCauseIsNotFoundThrowAnException() {
        UUID id = UUID.randomUUID();
        RefinedScenarioUpdateDto updateDto = assembleRefinedScenarioUpdateDto.get();
        RefinedScenario existingScenario = assembleRefinedScenario.get();
        existingScenario.setId(id);

        when(refinedScenarioRepository.findById(id)).thenReturn(Optional.of(existingScenario));
        when(commonCauseRepository.findById(updateDto.getCommonCauseId())).thenReturn(Optional.empty());

        assertThrows(Step4NewNotFoundException.class, () -> service.update(id, updateDto));
        
        verify(refinedScenarioRepository, times(1)).findById(id);
        verify(commonCauseRepository, times(1)).findById(updateDto.getCommonCauseId());
        verify(refinedScenarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("#update > When all inputs are valid > Update the RefinedScenario")
    void updateWhenAllInputsAreValidUpdateTheRefinedScenario() {
        UUID id = UUID.randomUUID();
        RefinedScenarioUpdateDto updateDto = assembleRefinedScenarioUpdateDto.get();
        RefinedScenario existingScenario = assembleRefinedScenario.get();
        existingScenario.setId(id);
        RefinedScenarioCommonCause commonCause = assembleCommonCause.apply("CC-2");

        when(refinedScenarioRepository.findById(id)).thenReturn(Optional.of(existingScenario));
        when(commonCauseRepository.findById(updateDto.getCommonCauseId())).thenReturn(Optional.of(commonCause));

        assertDoesNotThrow(() -> service.update(id, updateDto));

        verify(refinedScenarioRepository, times(1)).findById(id);
        verify(commonCauseRepository, times(1)).findById(updateDto.getCommonCauseId());
        verify(refinedScenarioRepository, times(1)).save(existingScenario);
    }

    @Test
    @DisplayName("#delete > When RefinedScenario is not found > Throw an exception")
    void deleteWhenRefinedScenarioIsNotFoundThrowAnException() {
        UUID id = UUID.randomUUID();

        when(refinedScenarioRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(Step4NewNotFoundException.class, () -> service.delete(id));
        
        verify(refinedScenarioRepository, times(1)).findById(id);
        verify(refinedScenarioRepository, never()).delete(any());
    }

    @Test
    @DisplayName("#delete > When RefinedScenario is found > Delete the RefinedScenario")
    void deleteWhenRefinedScenarioIsFoundDeleteTheRefinedScenario() {
        UUID id = UUID.randomUUID();
        RefinedScenario scenario = assembleRefinedScenario.get();
        scenario.setId(id);

        when(refinedScenarioRepository.findById(id)).thenReturn(Optional.of(scenario));

        assertDoesNotThrow(() -> service.delete(id));

        verify(refinedScenarioRepository, times(1)).findById(id);
        verify(refinedScenarioRepository, times(1)).delete(scenario);
    }

    private final Function<String, RefinedScenarioCommonCause> assembleCommonCause = (code) -> RefinedScenarioCommonCause.builder()
            .id(UUID.randomUUID())
            .code(code)
            .commonCause("Common Cause " + code)
            .build();

    private final Supplier<RefinedScenario> assembleRefinedScenario = () -> {
        FormalScenario formalScenario = FormalScenario.builder()
                .id(UUID.randomUUID())
                .analysisId(UUID.randomUUID())
                .unsafeControlActionId(UUID.randomUUID())
                .build();
        
        FormalScenarioClass formalScenarioClass = FormalScenarioClass.builder()
                .id(UUID.randomUUID())
                .code("CLASS1")
                .formalScenario(formalScenario)
                .build();

        return RefinedScenario.builder()
                .id(UUID.randomUUID())
                .refinedScenario("Test scenario")
                .code("RSC-1")
                .formalScenarioClass(formalScenarioClass)
                .commonCause(assembleCommonCause.apply("CC-1"))
                .build();
    };

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

    private final Function<UUID, RefinedScenarioInsertDto> assembleRefinedScenarioInsertDto = (commonCauseId) -> RefinedScenarioInsertDto.builder()
            .refinedScenario("Test scenario")
            .unsafeControlActionId(UUID.randomUUID())
            .commonCauseId(commonCauseId)
            .formalScenarioClassId(UUID.randomUUID())
            .build();

    private final Supplier<RefinedScenarioUpdateDto> assembleRefinedScenarioUpdateDto = () -> RefinedScenarioUpdateDto.builder()
            .refinedScenario("Updated scenario")
            .commonCauseId(UUID.randomUUID())
            .build();
}
