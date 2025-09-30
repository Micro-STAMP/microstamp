package microstamp.step4new.unit;

import microstamp.step3.dto.UnsafeControlActionFullReadDto;
import microstamp.step4new.client.MicroStampAuthClient;
import microstamp.step4new.client.MicroStampStep3Client;
import microstamp.step4new.dto.formalscenario.FormalScenarioReadDto;
import microstamp.step4new.entity.FormalScenario;
import microstamp.step4new.entity.FormalScenarioClass;
import microstamp.step4new.exception.Step4NewIllegalArgumentException;
import microstamp.step4new.exception.Step4NewNotFoundException;
import microstamp.step4new.repository.FormalScenarioRepository;
import microstamp.step4new.service.impl.FormalScenarioServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FormalScenarioServiceUnitTest {

    @InjectMocks
    private FormalScenarioServiceImpl service;

    @Mock
    private FormalScenarioRepository repository;

    @Mock
    private MicroStampStep3Client step3Client;

    @Mock
    private MicroStampAuthClient authClient;

    @Test
    @DisplayName("#create > When FormalScenario already exists for UCA > Throw an exception")
    void createWhenFormalScenarioAlreadyExistsForUcaThrowAnException() {
        UUID analysisId = UUID.randomUUID();
        UUID ucaId = UUID.randomUUID();
        FormalScenario existingScenario = assembleFormalScenario.apply(ucaId);

        when(repository.findByUnsafeControlActionId(ucaId)).thenReturn(Optional.of(existingScenario));

        assertThrows(Step4NewIllegalArgumentException.class, () -> service.create(analysisId, ucaId));
        
        verify(repository, times(1)).findByUnsafeControlActionId(ucaId);
        verify(authClient, never()).getAnalysisById(any());
        verify(step3Client, never()).readUnsafeControlAction(any());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("#create > When all inputs are valid > Create FormalScenario with 4 classes")
    void createWhenAllInputsAreValidCreateFormalScenarioWithFourClasses() {
        UUID analysisId = UUID.randomUUID();
        UUID ucaId = UUID.randomUUID();
        UnsafeControlActionFullReadDto ucaDto = assembleUcaDto.apply(ucaId);

        when(repository.findByUnsafeControlActionId(ucaId)).thenReturn(Optional.empty());
        when(authClient.getAnalysisById(analysisId)).thenReturn(null);
        when(step3Client.readUnsafeControlAction(ucaId)).thenReturn(ucaDto);
        when(repository.save(any(FormalScenario.class))).thenAnswer(invocation -> {
            FormalScenario fs = invocation.getArgument(0);
            if (fs.getId() == null) fs.setId(UUID.randomUUID());

            fs.getClasses().forEach(clazz -> {
                if (clazz.getId() == null) clazz.setId(UUID.randomUUID());
            });
            return fs;
        });

        FormalScenarioReadDto response = service.create(analysisId, ucaId);

        assertAll(
                () -> assertNotNull(response),
                () -> assertNotNull(response.getClass1()),
                () -> assertNotNull(response.getClass2()),
                () -> assertNotNull(response.getClass3()),
                () -> assertNotNull(response.getClass4())
        );

        verify(repository, times(1)).findByUnsafeControlActionId(ucaId);
        verify(authClient, times(1)).getAnalysisById(analysisId);
        verify(step3Client, times(1)).readUnsafeControlAction(ucaId);
        verify(repository, times(1)).save(any(FormalScenario.class));
    }

    @Test
    @DisplayName("#getOrCreate > When FormalScenario exists > Return existing")
    void getOrCreateWhenFormalScenarioExistsReturnExisting() {
        UUID ucaId = UUID.randomUUID();
        FormalScenario existingScenario = assembleFormalScenario.apply(ucaId);
        UnsafeControlActionFullReadDto ucaDto = assembleUcaDto.apply(ucaId);

        when(step3Client.readUnsafeControlAction(ucaId)).thenReturn(ucaDto);
        when(repository.findByUnsafeControlActionId(ucaId)).thenReturn(Optional.of(existingScenario));

        FormalScenarioReadDto response = service.getOrCreate(ucaId);

        assertNotNull(response);
        verify(repository, times(1)).findByUnsafeControlActionId(ucaId);
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("#getOrCreate > When FormalScenario does not exist > Create new one")
    void getOrCreateWhenFormalScenarioDoesNotExistCreateNewOne() {
        UUID ucaId = UUID.randomUUID();
        UnsafeControlActionFullReadDto ucaDto = assembleUcaDto.apply(ucaId);

        when(step3Client.readUnsafeControlAction(ucaId)).thenReturn(ucaDto);
        when(repository.findByUnsafeControlActionId(ucaId)).thenReturn(Optional.empty());
        when(authClient.getAnalysisById(ucaDto.analysis_id())).thenReturn(null);
        when(repository.save(any(FormalScenario.class))).thenAnswer(invocation -> {
            FormalScenario fs = invocation.getArgument(0);
            if (fs.getId() == null) fs.setId(UUID.randomUUID());

            fs.getClasses().forEach(clazz -> {
                if (clazz.getId() == null) clazz.setId(UUID.randomUUID());
            });
            return fs;
        });

        FormalScenarioReadDto response = service.getOrCreate(ucaId);

        assertAll(
                () -> assertNotNull(response),
                () -> assertNotNull(response.getClass1()),
                () -> assertNotNull(response.getClass2()),
                () -> assertNotNull(response.getClass3()),
                () -> assertNotNull(response.getClass4())
        );

        verify(repository, times(2)).findByUnsafeControlActionId(ucaId);
        verify(repository, times(1)).save(any(FormalScenario.class));
    }

    @Test
    @DisplayName("#findById > When FormalScenario is not found > Throw an exception")
    void findByIdWhenFormalScenarioIsNotFoundThrowAnException() {
        UUID id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(Step4NewNotFoundException.class, () -> service.findById(id));
        
        verify(repository, times(1)).findById(id);
        verify(step3Client, never()).readUnsafeControlAction(any());
    }

    @Test
    @DisplayName("#findById > When FormalScenario is found > Return mapped DTO")
    void findByIdWhenFormalScenarioIsFoundReturnMappedDto() {
        UUID id = UUID.randomUUID();
        UUID ucaId = UUID.randomUUID();
        FormalScenario scenario = assembleFormalScenario.apply(ucaId);
        scenario.setId(id);
        UnsafeControlActionFullReadDto ucaDto = assembleUcaDto.apply(ucaId);

        when(repository.findById(id)).thenReturn(Optional.of(scenario));
        when(step3Client.readUnsafeControlAction(ucaId)).thenReturn(ucaDto);

        FormalScenarioReadDto response = service.findById(id);

        assertNotNull(response);
        verify(repository, times(1)).findById(id);
        verify(step3Client, times(1)).readUnsafeControlAction(ucaId);
    }

    @Test
    @DisplayName("#delete > When called with valid id > Delete the FormalScenario")
    void deleteWhenCalledWithValidIdDeleteTheFormalScenario() {
        UUID id = UUID.randomUUID();

        service.delete(id);

        verify(repository, times(1)).deleteById(id);
    }

    private final Function<UUID, FormalScenario> assembleFormalScenario = (ucaId) -> {
        FormalScenario scenario = FormalScenario.builder()
                .id(UUID.randomUUID())
                .unsafeControlActionId(ucaId)
                .analysisId(UUID.randomUUID())
                .build();

        scenario.addClass(FormalScenarioClass.builder()
                .id(UUID.randomUUID())
                .code("[CLASS-1]")
                .build());
        scenario.addClass(FormalScenarioClass.builder()
                .id(UUID.randomUUID())
                .code("[CLASS-2]")
                .build());
        scenario.addClass(FormalScenarioClass.builder()
                .id(UUID.randomUUID())
                .code("[CLASS-3]")
                .build());
        scenario.addClass(FormalScenarioClass.builder()
                .id(UUID.randomUUID())
                .code("[CLASS-4]")
                .build());
                
        return scenario;
    };

    private final Function<UUID, UnsafeControlActionFullReadDto> assembleUcaDto = (ucaId) -> UnsafeControlActionFullReadDto.builder()
            .id(ucaId)
            .analysis_id(UUID.randomUUID())
            .name("Test UCA")
            .uca_code("UCA-1")
            .type(microstamp.step3.dto.UCAType.PROVIDED)
            .context("test context")
            .control_action(microstamp.step2.dto.ControlActionReadDto.builder()
                    .id(UUID.randomUUID())
                    .name("Test Control Action")
                    .code("CA-1")
                    .connection(microstamp.step2.dto.ConnectionReadDto.builder()
                            .id(UUID.randomUUID())
                            .code("CONN-1")
                            .source(microstamp.step2.dto.ComponentReadDto.builder()
                                    .id(UUID.randomUUID())
                                    .name("Source Component")
                                    .build())
                            .target(microstamp.step2.dto.ComponentReadDto.builder()
                                    .id(UUID.randomUUID())
                                    .name("Target Component")
                                    .build())
                            .build())
                    .build())
            .build();
}
