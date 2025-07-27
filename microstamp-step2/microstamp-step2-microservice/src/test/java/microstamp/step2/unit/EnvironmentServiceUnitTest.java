package microstamp.step2.unit;

import microstamp.step2.client.MicroStampAuthClient;
import microstamp.step2.dto.environment.EnvironmentReadDto;
import microstamp.step2.entity.Environment;
import microstamp.step2.exception.Step2NotFoundException;
import microstamp.step2.exception.Step2UniqueEnvironmentException;
import microstamp.step2.repository.ConnectionRepository;
import microstamp.step2.repository.EnvironmentRepository;
import microstamp.step2.service.impl.EnvironmentServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EnvironmentServiceUnitTest {

    @InjectMocks
    private EnvironmentServiceImpl service;

    @Mock
    private MicroStampAuthClient microStampAuthClient;

    @Mock
    private EnvironmentRepository environmentRepository;

    @Mock
    private ConnectionRepository connectionRepository;

    @Test
    @DisplayName("#findById > When no environment is found > Throw an exception")
    void findByIdWhenNoEnvironmentIsFoundThrowAnException() {
        UUID environmentUUID = UUID.randomUUID();
        when(environmentRepository.findById(environmentUUID)).thenReturn(Optional.empty());

        assertThrows(Step2NotFoundException.class, () -> service.findById(environmentUUID));
    }

    @Test
    @DisplayName("#findById > When the environment is found > Return it")
    void findByIdWhenTheEnvironmentIsFoundReturnIt() {
        Environment mockEnvironment = assembleEnvironment.get();

        when(environmentRepository.findById(mockEnvironment.getId())).thenReturn(Optional.of(mockEnvironment));

        EnvironmentReadDto response = service.findById(mockEnvironment.getId());
        assertEquals(mockEnvironment.getId(), response.getId());
    }

    @Test
    @DisplayName("#findByAnalysisId > When the environment is found > Return it")
    void findByAnalysisIdWhenTheEnvironmentIsFoundReturnIt() {
        UUID analysisUUID = UUID.randomUUID();
        Environment mockEnvironment = assembleEnvironment.get();

        when(environmentRepository.findByAnalysisId(analysisUUID)).thenReturn(mockEnvironment);

        EnvironmentReadDto response = service.findByAnalysisId(analysisUUID);
        assertEquals(mockEnvironment.getId(), response.getId());
    }

    @Test
    @DisplayName("#insert > When the environment is found > Throw an exception ")
    void insertWhenTheEnvironmentIsFoundThrowAnException() {
        UUID analysisId = UUID.randomUUID();
        Environment mockEnvironment = assembleEnvironment.get();
        when(environmentRepository.findByAnalysisId(analysisId)).thenReturn(mockEnvironment);

        assertThrows(Step2UniqueEnvironmentException.class, () -> service.insert(analysisId));
    }

    @Test
    @DisplayName("#insert > When the environment is not found > Insert the environment on database")
    void insertWhenTheEnvironmentIsNotFoundInsertTheEnvironmentOnDatabase() {
        UUID analysisId = UUID.randomUUID();
        Environment mockEnvironment = assembleEnvironment.get();
        when(environmentRepository.findByAnalysisId(analysisId)).thenReturn(null);

        EnvironmentReadDto response = service.insert(analysisId);

        assertAll(
                () -> assertEquals(mockEnvironment.getName(), response.getName()),
                () -> assertEquals(mockEnvironment.getCode(), response.getCode()),
                () -> assertEquals(mockEnvironment.getIsVisible(), response.getIsVisible()),
                () -> assertEquals(mockEnvironment.getBorder(), response.getBorder()),
                () -> verify(environmentRepository, times(1)).save(any())
        );
    }

    @Test
    @DisplayName("#delete > When the environment is not found > Throw an exception")
    void deleteWhenTheEnvironmentIsNotFoundThrowAnException() {
        UUID environmentUUID = UUID.randomUUID();
        when(environmentRepository.findById(environmentUUID)).thenReturn(Optional.empty());

        assertThrows(Step2NotFoundException.class, () -> service.delete(environmentUUID));
    }

    @Test
    @DisplayName("#delete > When the environment is found > Delete the environment and the connections")
    void deleteWhenTheEnvironmentIsFoundDeleteTheEnvironmentAndTheConnections() {
        Environment mockEnvironment = assembleEnvironment.get();

        when(environmentRepository.findById(mockEnvironment.getId())).thenReturn(Optional.of(mockEnvironment));

        service.delete(mockEnvironment.getId());
        verify(environmentRepository, times(1)).deleteById(mockEnvironment.getId());
    }

    private final Supplier<Environment> assembleEnvironment = () -> Environment.builder().build();

}