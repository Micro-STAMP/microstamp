package microstamp.step1.unit;

import microstamp.step1.client.MicroStampClient;
import microstamp.step1.dto.systemgoal.SystemGoalInsertDto;
import microstamp.step1.dto.systemgoal.SystemGoalReadDto;
import microstamp.step1.dto.systemgoal.SystemGoalUpdateDto;
import microstamp.step1.entity.SystemGoal;
import microstamp.step1.exception.Step1IllegalArgumentException;
import microstamp.step1.exception.Step1NotFoundException;
import microstamp.step1.repository.SystemGoalRepository;
import microstamp.step1.service.impl.SystemGoalServiceImpl;
import org.junit.jupiter.api.Assertions;
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
public class SystemGoalServiceUnitTest {


    @InjectMocks
    private SystemGoalServiceImpl service;

    @Mock
    private SystemGoalRepository repository;

    @Mock
    private MicroStampClient microStampClient;

    @Test
    @DisplayName("#findAll > When no system goal is found > Return an empty list")
    void findAllWhenNoSystemGoalIsFoundReturnAnEmptyList() {
        when(repository.findAll()).thenReturn(List.of());
        List<SystemGoalReadDto> systemGoals = service.findAll();

        assertTrue(systemGoals.isEmpty());
    }

    @Test
    @DisplayName("#findAll > When there are system goals found > Return the system goals")
    void findAllWhenThereAreSystemGoalsFoundReturnTheSystemGoals() {
        when(repository.findAll()).thenReturn(List.of(
                assembleSystemGoals.apply(1), assembleSystemGoals.apply(2)
        ));
        List<SystemGoalReadDto> systemGoal = service.findAll();

        assertAll(
                () -> Assertions.assertFalse(systemGoal.isEmpty()),
                () -> Assertions.assertEquals(2, systemGoal.size()),
                () -> Assertions.assertEquals("Name 1", systemGoal.getFirst().getName()),
                () -> Assertions.assertEquals("Code 1", systemGoal.getFirst().getCode()),
                () -> Assertions.assertEquals("Name 2", systemGoal.get(1).getName()),
                () -> Assertions.assertEquals("Code 2", systemGoal.get(1).getCode())
        );
    }

    @Test
    @DisplayName("#findById > When no system goal is found > Throw an exception")
    void findByIdWhenNoSystemGoalIsFoundThrowAnException() {
        UUID mockSystemGoalId = UUID.randomUUID();
        when(repository.findById(mockSystemGoalId)).thenReturn(Optional.empty());

        assertThrows(Step1NotFoundException.class, () -> service.findById(mockSystemGoalId));
    }

    @Test
    @DisplayName("#findById > When the system goal is found > Return the system goal")
    void findByIdWhenTheSystemGoalIsFoundReturnTheSystemGoal() {
        UUID mockSystemGoalId = UUID.randomUUID();
        SystemGoal mock = assembleSystemGoals.apply(1);
        mock.setId(mockSystemGoalId);
        when(repository.findById(mockSystemGoalId)).thenReturn(Optional.of(mock));

        SystemGoalReadDto response = service.findById(mockSystemGoalId);
        assertAll(
                () -> assertEquals(mock.getId(), response.getId()),
                () -> assertEquals(mock.getName(), response.getName()),
                () -> assertEquals(mock.getCode(), response.getCode())
        );
    }

    @Test
    @DisplayName("#findByAnalysisId > When no system goal is found > Return an empty list")
    void findByAnalysisIdWhenNoSystemGoalIsFoundReturnAnEmptyList() {
        UUID mockAnalysisId = UUID.randomUUID();
        when(repository.findByAnalysisId(mockAnalysisId)).thenReturn(List.of());

        List<SystemGoalReadDto> systemGoals = service.findByAnalysisId(mockAnalysisId);

        assertTrue(systemGoals.isEmpty());
    }

    @Test
    @DisplayName("#findByAnalysisId > When there are system goals found > Return the system goals")
    void findByAnalysisIdWhenThereAreSystemGoalsFoundReturnTheSystemGoals() {
        UUID mockAnalysisId = UUID.randomUUID();
        when(repository.findByAnalysisId(mockAnalysisId)).thenReturn(List.of(
                assembleSystemGoals.apply(1), assembleSystemGoals.apply(2)
        ));

        List<SystemGoalReadDto> systemGoals = service.findByAnalysisId(mockAnalysisId);

        assertAll(
                () -> Assertions.assertFalse(systemGoals.isEmpty()),
                () -> Assertions.assertEquals(2, systemGoals.size()),
                () -> Assertions.assertEquals("Name 1", systemGoals.getFirst().getName()),
                () -> Assertions.assertEquals("Code 1", systemGoals.getFirst().getCode()),
                () -> Assertions.assertEquals("Name 2", systemGoals.get(1).getName()),
                () -> Assertions.assertEquals("Code 2", systemGoals.get(1).getCode())
        );
    }

    @Test
    @DisplayName("#insert > When the input is invalid > Throw an exception")
    void insertWhenTheInputIsInvalidThrowAnException() {
        assertThrows(Step1IllegalArgumentException.class, () -> service.insert( null));
    }

    @Test
    @DisplayName("#insert > When the input are valid > Create the system goal")
    void insertWhenTheInputAreValidCreateTheSystemGoal() {
        SystemGoalInsertDto mockSystemGoalInsertDto = assembleSystemGoalInsertDto.get();

        when(microStampClient.getAnalysisById(mockSystemGoalInsertDto.getAnalysisId())).thenReturn(null);
        when(repository.save(any(SystemGoal.class))).thenAnswer(answer -> answer.getArguments()[0]);

        SystemGoalReadDto response = service.insert( mockSystemGoalInsertDto);
        assertAll(
                () -> assertEquals(mockSystemGoalInsertDto.getName(), response.getName()),
                () -> assertEquals(mockSystemGoalInsertDto.getCode(), response.getCode())
        );
    }

    @Test
    @DisplayName("#update > When the input is invalid > Throw an exception")
    void updateWhenTheInputIsInvalidThrowAnException() {
        assertAll(
                () -> assertThrows(Step1IllegalArgumentException.class, () -> service.update(null, null)),
                () -> assertThrows(Step1IllegalArgumentException.class, () -> service.update(UUID.randomUUID(), null))
        );
    }

    @Test
    @DisplayName("#update > When the analysis does not exist > Throw an exception")
    void updateWhenTheAnalysisDoesNotExistThrowAnException() {
        UUID mockSystemGoalId = UUID.randomUUID();
        when(repository.findById(mockSystemGoalId)).thenReturn(Optional.empty());

        assertThrows(Step1NotFoundException.class, () -> service.update(mockSystemGoalId, assembleSystemGoalUpdateDto.get()));
    }

    @Test
    @DisplayName("#update > When the analysis exist > Update the system goal")
    void updateWhenTheAnalysisExistUpdateTheSystemGoal() {
        UUID mockSystemGoalId = UUID.randomUUID();
        SystemGoal mockSystemGoal = assembleSystemGoals.apply(1);
        SystemGoalUpdateDto mockSystemGoalUpdate = assembleSystemGoalUpdateDto.get();
        when(repository.findById(mockSystemGoalId)).thenReturn(Optional.of(mockSystemGoal));
        mockSystemGoal.setName(mockSystemGoalUpdate.getName());

        service.update(mockSystemGoalId, mockSystemGoalUpdate);

        assertAll(
                () -> verify(repository, times(1)).findById(mockSystemGoalId),
                () -> verify(repository, times(1)).save(mockSystemGoal)
        );
    }

    @Test
    @DisplayName("#delete > When no system goal is found > Throw an exception")
    void deleteWhenNoSystemGoalIsFoundThrowAnException() {
        UUID mockSystemGoalId = UUID.randomUUID();
        when(repository.findById(mockSystemGoalId)).thenReturn(Optional.empty());

        assertThrows(Step1NotFoundException.class, () -> service.delete(mockSystemGoalId));
    }

    @Test
    @DisplayName("#delete > When the system goal is found > Delete the system goal")
    void deleteWhenTheSystemGoalIsFoundDeleteTheSystemGoal() {
        UUID mockSystemGoalId = UUID.randomUUID();
        SystemGoal mockSystemGoal= assembleSystemGoals.apply(1);
        mockSystemGoal.setId(mockSystemGoalId);
        when(repository.findById(mockSystemGoalId)).thenReturn(Optional.of(mockSystemGoal));

        service.delete(mockSystemGoalId);

        assertAll(
                () -> verify(repository, times(1)).findById(mockSystemGoalId),
                () -> verify(repository, times(1)).deleteById(mockSystemGoalId)
        );
    }

    private final Function<Integer, SystemGoal> assembleSystemGoals = (index) -> SystemGoal.builder()
            .id(UUID.randomUUID())
            .name("Name " + index)
            .code("Code " + index)
            .build();

    private final Supplier<SystemGoalInsertDto> assembleSystemGoalInsertDto = () -> SystemGoalInsertDto.builder()
            .name("Insert")
            .code("Code")
            .analysisId(UUID.randomUUID())
            .build();

    private final Supplier<SystemGoalUpdateDto> assembleSystemGoalUpdateDto = () -> SystemGoalUpdateDto.builder()
            .name("Insert")
            .code("Code")
            .build();
}
