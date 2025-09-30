package microstamp.step4new.unit;

import microstamp.step4new.dto.mitigation.MitigationInsertDto;
import microstamp.step4new.dto.mitigation.MitigationReadDto;
import microstamp.step4new.dto.mitigation.MitigationUpdateDto;
import microstamp.step4new.entity.FormalScenario;
import microstamp.step4new.entity.FormalScenarioClass;
import microstamp.step4new.entity.Mitigation;
import microstamp.step4new.entity.RefinedScenario;
import microstamp.step4new.exception.Step4NewIllegalArgumentException;
import microstamp.step4new.exception.Step4NewNotFoundException;
import microstamp.step4new.repository.MitigationRepository;
import microstamp.step4new.repository.RefinedScenarioRepository;
import microstamp.step4new.service.impl.MitigationServiceImpl;
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
public class MitigationServiceUnitTest {

    @InjectMocks
    private MitigationServiceImpl service;

    @Mock
    private MitigationRepository mitigationRepository;

    @Mock
    private RefinedScenarioRepository refinedScenarioRepository;

    @Test
    @DisplayName("#findById > When Mitigation is not found > Throw an exception")
    void findByIdWhenMitigationIsNotFoundThrowAnException() {
        UUID id = UUID.randomUUID();

        when(mitigationRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(Step4NewNotFoundException.class, () -> service.findById(id));
        
        verify(mitigationRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("#findById > When Mitigation is found > Return mapped DTO")
    void findByIdWhenMitigationIsFoundReturnMappedDto() {
        UUID id = UUID.randomUUID();
        Mitigation mitigation = assembleMitigation.apply(id);

        when(mitigationRepository.findById(id)).thenReturn(Optional.of(mitigation));

        MitigationReadDto response = service.findById(id);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(mitigation.getId(), response.getId()),
                () -> assertEquals(mitigation.getMitigation(), response.getMitigation()),
                () -> assertEquals(mitigation.getCode(), response.getCode())
        );
        
        verify(mitigationRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("#getOrCreateByRefinedScenarioId > When Mitigation exists > Return existing")
    void getOrCreateByRefinedScenarioIdWhenMitigationExistsReturnExisting() {
        UUID refinedScenarioId = UUID.randomUUID();
        Mitigation existingMitigation = assembleMitigation.apply(UUID.randomUUID());

        when(mitigationRepository.findByRefinedScenarioId(refinedScenarioId)).thenReturn(Optional.of(existingMitigation));

        MitigationReadDto response = service.getOrCreateByRefinedScenarioId(refinedScenarioId);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(existingMitigation.getId(), response.getId())
        );
        
        verify(mitigationRepository, times(1)).findByRefinedScenarioId(refinedScenarioId);
        verify(refinedScenarioRepository, never()).findById(any());
        verify(mitigationRepository, never()).save(any());
    }

    @Test
    @DisplayName("#getOrCreateByRefinedScenarioId > When Mitigation does not exist > Create new one")
    void getOrCreateByRefinedScenarioIdWhenMitigationDoesNotExistCreateNewOne() {
        UUID refinedScenarioId = UUID.randomUUID();
        RefinedScenario refinedScenario = assembleRefinedScenario.get();
        refinedScenario.setId(refinedScenarioId);

        when(mitigationRepository.findByRefinedScenarioId(refinedScenarioId)).thenReturn(Optional.empty());
        when(refinedScenarioRepository.findById(refinedScenarioId)).thenReturn(Optional.of(refinedScenario));
        when(mitigationRepository.findMaxCodeNumberByAnalysisId(any(UUID.class))).thenReturn(null);
        when(mitigationRepository.save(any(Mitigation.class))).thenAnswer(invocation -> {
            Mitigation m = invocation.getArgument(0);
            if (m.getId() == null) m.setId(UUID.randomUUID());
            return m;
        });

        MitigationReadDto response = service.getOrCreateByRefinedScenarioId(refinedScenarioId);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals("RSOL-1", response.getCode()),
                () -> assertEquals("", response.getMitigation())
        );

        verify(mitigationRepository, times(2)).findByRefinedScenarioId(refinedScenarioId); // Called once in getOrCreate and once in create
        verify(refinedScenarioRepository, times(1)).findById(refinedScenarioId);
        verify(mitigationRepository, times(1)).save(any(Mitigation.class));
    }

    @Test
    @DisplayName("#findByUnsafeControlActionId > When no Mitigations are found > Return empty list")
    void findByUnsafeControlActionIdWhenNoMitigationsAreFoundReturnEmptyList() {
        UUID ucaId = UUID.randomUUID();

        when(mitigationRepository.findByRefinedScenario_UnsafeControlActionId(ucaId)).thenReturn(List.of());

        List<MitigationReadDto> response = service.findByUnsafeControlActionId(ucaId);

        assertTrue(response.isEmpty());
        verify(mitigationRepository, times(1)).findByRefinedScenario_UnsafeControlActionId(ucaId);
    }

    @Test
    @DisplayName("#findByUnsafeControlActionId > When Mitigations are found > Return mapped DTOs")
    void findByUnsafeControlActionIdWhenMitigationsAreFoundReturnMappedDtos() {
        UUID ucaId = UUID.randomUUID();
        Mitigation mitigation1 = assembleMitigation.apply(UUID.randomUUID());
        Mitigation mitigation2 = assembleMitigation.apply(UUID.randomUUID());

        when(mitigationRepository.findByRefinedScenario_UnsafeControlActionId(ucaId)).thenReturn(List.of(mitigation1, mitigation2));

        List<MitigationReadDto> response = service.findByUnsafeControlActionId(ucaId);

        assertAll(
                () -> assertEquals(2, response.size()),
                () -> assertEquals(mitigation1.getId(), response.getFirst().getId()),
                () -> assertEquals(mitigation2.getId(), response.get(1).getId())
        );
        
        verify(mitigationRepository, times(1)).findByRefinedScenario_UnsafeControlActionId(ucaId);
    }

    @Test
    @DisplayName("#create > When RefinedScenario is not found > Throw an exception")
    void createWhenRefinedScenarioIsNotFoundThrowAnException() {
        UUID refinedScenarioId = UUID.randomUUID();
        MitigationInsertDto insertDto = assembleMitigationInsertDto.apply(refinedScenarioId);

        when(refinedScenarioRepository.findById(refinedScenarioId)).thenReturn(Optional.empty());

        assertThrows(Step4NewNotFoundException.class, () -> service.create(insertDto));
        
        verify(refinedScenarioRepository, times(1)).findById(refinedScenarioId);
        verify(mitigationRepository, never()).findByRefinedScenarioId(any());
        verify(mitigationRepository, never()).save(any());
    }

    @Test
    @DisplayName("#create > When Mitigation already exists for RefinedScenario > Throw an exception")
    void createWhenMitigationAlreadyExistsForRefinedScenarioThrowAnException() {
        UUID refinedScenarioId = UUID.randomUUID();
        MitigationInsertDto insertDto = assembleMitigationInsertDto.apply(refinedScenarioId);
        RefinedScenario refinedScenario = assembleRefinedScenario.get();
        Mitigation existingMitigation = assembleMitigation.apply(UUID.randomUUID());

        when(refinedScenarioRepository.findById(refinedScenarioId)).thenReturn(Optional.of(refinedScenario));
        when(mitigationRepository.findByRefinedScenarioId(refinedScenarioId)).thenReturn(Optional.of(existingMitigation));

        assertThrows(Step4NewIllegalArgumentException.class, () -> service.create(insertDto));
        
        verify(refinedScenarioRepository, times(1)).findById(refinedScenarioId);
        verify(mitigationRepository, times(1)).findByRefinedScenarioId(refinedScenarioId);
        verify(mitigationRepository, never()).save(any());
    }

    @Test
    @DisplayName("#create > When all inputs are valid > Create Mitigation with generated code")
    void createWhenAllInputsAreValidCreateMitigationWithGeneratedCode() {
        UUID refinedScenarioId = UUID.randomUUID();
        MitigationInsertDto insertDto = assembleMitigationInsertDto.apply(refinedScenarioId);
        RefinedScenario refinedScenario = assembleRefinedScenario.get();

        when(refinedScenarioRepository.findById(refinedScenarioId)).thenReturn(Optional.of(refinedScenario));
        when(mitigationRepository.findByRefinedScenarioId(refinedScenarioId)).thenReturn(Optional.empty());
        when(mitigationRepository.findMaxCodeNumberByAnalysisId(any(UUID.class))).thenReturn(5);
        when(mitigationRepository.save(any(Mitigation.class))).thenAnswer(invocation -> {
            Mitigation m = invocation.getArgument(0);
            if (m.getId() == null) m.setId(UUID.randomUUID());
            return m;
        });

        MitigationReadDto response = service.create(insertDto);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals("RSOL-6", response.getCode()),
                () -> assertEquals(insertDto.getMitigation(), response.getMitigation())
        );

        verify(refinedScenarioRepository, times(1)).findById(refinedScenarioId);
        verify(mitigationRepository, times(1)).findByRefinedScenarioId(refinedScenarioId);
        verify(mitigationRepository, times(1)).save(any(Mitigation.class));
    }

    @Test
    @DisplayName("#update > When Mitigation is not found > Throw an exception")
    void updateWhenMitigationIsNotFoundThrowAnException() {
        UUID id = UUID.randomUUID();
        MitigationUpdateDto updateDto = assembleMitigationUpdateDto.get();

        when(mitigationRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(Step4NewNotFoundException.class, () -> service.update(id, updateDto));
        
        verify(mitigationRepository, times(1)).findById(id);
        verify(mitigationRepository, never()).save(any());
    }

    @Test
    @DisplayName("#update > When Mitigation is found > Update the Mitigation")
    void updateWhenMitigationIsFoundUpdateTheMitigation() {
        UUID id = UUID.randomUUID();
        Mitigation existingMitigation = assembleMitigation.apply(id);
        MitigationUpdateDto updateDto = assembleMitigationUpdateDto.get();

        when(mitigationRepository.findById(id)).thenReturn(Optional.of(existingMitigation));

        assertDoesNotThrow(() -> service.update(id, updateDto));

        verify(mitigationRepository, times(1)).findById(id);
        verify(mitigationRepository, times(1)).save(existingMitigation);
    }

    @Test
    @DisplayName("#delete > When Mitigation is not found > Throw an exception")
    void deleteWhenMitigationIsNotFoundThrowAnException() {
        UUID id = UUID.randomUUID();

        when(mitigationRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(Step4NewNotFoundException.class, () -> service.delete(id));
        
        verify(mitigationRepository, times(1)).findById(id);
        verify(mitigationRepository, never()).delete(any());
    }

    @Test
    @DisplayName("#delete > When Mitigation is found > Delete the Mitigation")
    void deleteWhenMitigationIsFoundDeleteTheMitigation() {
        UUID id = UUID.randomUUID();
        Mitigation mitigation = assembleMitigation.apply(id);

        when(mitigationRepository.findById(id)).thenReturn(Optional.of(mitigation));

        assertDoesNotThrow(() -> service.delete(id));

        verify(mitigationRepository, times(1)).findById(id);
        verify(mitigationRepository, times(1)).delete(mitigation);
    }

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
                .build();
    };

    private final Function<UUID, Mitigation> assembleMitigation = (id) -> Mitigation.builder()
            .id(id)
            .mitigation("Test mitigation")
            .code("RSOL-1")
            .refinedScenario(assembleRefinedScenario.get())
            .build();

    private final Function<UUID, MitigationInsertDto> assembleMitigationInsertDto = (refinedScenarioId) -> MitigationInsertDto.builder()
            .refinedScenarioId(refinedScenarioId)
            .mitigation("Test mitigation")
            .build();

    private final Supplier<MitigationUpdateDto> assembleMitigationUpdateDto = () -> MitigationUpdateDto.builder()
            .mitigation("Updated mitigation")
            .build();
}
