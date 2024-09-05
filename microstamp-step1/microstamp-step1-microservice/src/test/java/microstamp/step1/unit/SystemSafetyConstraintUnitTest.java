package microstamp.step1.unit;

import feign.FeignException;
import microstamp.step1.client.MicroStampClient;
import microstamp.step1.dto.systemsafetyconstraint.SystemSafetyConstraintInsertDto;
import microstamp.step1.dto.systemsafetyconstraint.SystemSafetyConstraintReadDto;
import microstamp.step1.dto.systemsafetyconstraint.SystemSafetyConstraintUpdateDto;
import microstamp.step1.entity.Hazard;
import microstamp.step1.entity.SystemSafetyConstraint;
import microstamp.step1.exception.Step1IllegalArgumentException;
import microstamp.step1.exception.Step1NotFoundException;
import microstamp.step1.repository.HazardRepository;
import microstamp.step1.repository.SystemSafetyConstraintRepository;
import microstamp.step1.service.impl.SystemSafetyConstraintServiceImpl;
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
public class SystemSafetyConstraintUnitTest {

    @InjectMocks
    private SystemSafetyConstraintServiceImpl service;

    @Mock
    private SystemSafetyConstraintRepository repository;

    @Mock
    private HazardRepository hazardRepository;

    @Mock
    private MicroStampClient microStampClient;

    @Test
    @DisplayName("#findAll > When no system safety constraint is found > Return an empty list")
    void findAllWhenNoSystemSafetyConstraintIsFoundReturnAnEmptyList() {
        when(repository.findAll()).thenReturn(List.of());
        List<SystemSafetyConstraintReadDto> systemSafetyConstraints = service.findAll();

        assertTrue(systemSafetyConstraints.isEmpty());
    }

    @Test
    @DisplayName("#findAll > When there are system safety constraints found > Return the system safety constraints")
    void findAllWhenThereAreSystemSafetyConstraintsFoundReturnTheSystemSafetyConstraints() {
        when(repository.findAll()).thenReturn(List.of(
                assembleSystemSafetyConstraint.apply(1), assembleSystemSafetyConstraint.apply(2)
        ));
        List<SystemSafetyConstraintReadDto> systemSafetyConstraints = service.findAll();

        assertAll(
                () -> Assertions.assertFalse(systemSafetyConstraints.isEmpty()),
                () -> Assertions.assertEquals(2, systemSafetyConstraints.size()),
                () -> Assertions.assertEquals("Name 1", systemSafetyConstraints.getFirst().getName()),
                () -> Assertions.assertEquals("Code 1", systemSafetyConstraints.getFirst().getCode()),
                () -> Assertions.assertEquals("Name 2", systemSafetyConstraints.get(1).getName()),
                () -> Assertions.assertEquals("Code 2", systemSafetyConstraints.get(1).getCode())
        );
    }

    @Test
    @DisplayName("#findById > When no system safety constraints is found > Throw an exception")
    void findByIdWhenNoSystemSafetyConstraintsIsFoundThrowAnException() {
        UUID mockSystemSafetyConstraintId = UUID.randomUUID();
        when(repository.findById(mockSystemSafetyConstraintId)).thenReturn(Optional.empty());

        assertThrows(Step1NotFoundException.class, () -> service.findById(mockSystemSafetyConstraintId));
    }

    @Test
    @DisplayName("#findById > When the system safety constraint is found > Return the system safety constraint")
    void findByIdWhenTheSystemSafetyConstraintIsFoundReturnTheSystemSafetyConstraint() {
        UUID mockSystemSafetyConstraintId = UUID.randomUUID();
        SystemSafetyConstraint mock = assembleSystemSafetyConstraint.apply(1);
        mock.setId(mockSystemSafetyConstraintId);
        when(repository.findById(mockSystemSafetyConstraintId)).thenReturn(Optional.of(mock));

        SystemSafetyConstraintReadDto response = service.findById(mockSystemSafetyConstraintId);
        assertAll(
                () -> assertEquals(mock.getId(), response.getId()),
                () -> assertEquals(mock.getName(), response.getName()),
                () -> assertEquals(mock.getCode(), response.getCode())
        );
    }

    @Test
    @DisplayName("#findByAnalysisId > When no system safety constraint is found > Return an empty list")
    void findByAnalysisIdWhenNoSystemSafetyConstraintIsFoundReturnAnEmptyList() {
        UUID mockAnalysisId = UUID.randomUUID();
        when(repository.findByAnalysisId(mockAnalysisId)).thenReturn(List.of());

        List<SystemSafetyConstraintReadDto> systemSafetyConstraints = service.findByAnalysisId(mockAnalysisId);

        assertTrue(systemSafetyConstraints.isEmpty());
    }

    @Test
    @DisplayName("#findByAnalysisId > When there are system safety constraint found > Return the system safety constraints")
    void findByAnalysisIdWhenThereAreSystemSafetyConstraintFoundReturnTheSystemSafetyConstraints() {
        UUID mockAnalysisId = UUID.randomUUID();
        when(repository.findByAnalysisId(mockAnalysisId)).thenReturn(List.of(
                assembleSystemSafetyConstraint.apply(1), assembleSystemSafetyConstraint.apply(2)
        ));

        List<SystemSafetyConstraintReadDto> systemSafetyConstraints = service.findByAnalysisId(mockAnalysisId);

        assertAll(
                () -> Assertions.assertFalse(systemSafetyConstraints.isEmpty()),
                () -> Assertions.assertEquals(2, systemSafetyConstraints.size()),
                () -> Assertions.assertEquals("Name 1", systemSafetyConstraints.getFirst().getName()),
                () -> Assertions.assertEquals("Code 1", systemSafetyConstraints.getFirst().getCode()),
                () -> Assertions.assertEquals("Name 2", systemSafetyConstraints.get(1).getName()),
                () -> Assertions.assertEquals("Code 2", systemSafetyConstraints.get(1).getCode())
        );
    }

    @Test
    @DisplayName("#insert > When the input is invalid > Throw an exception")
    void insertWhenTheInputIsInvalidThrowAnException() {
        assertThrows(Step1IllegalArgumentException.class, () -> service.insert(null));
    }

    @Test
    @DisplayName("#insert > When there is an error when finding the analysis > Throw an exception")
    void insertWhenThereIsAnErrorWhenFindingTheAnalysisThrowAnException() {
        UUID mockAnalysisId = UUID.randomUUID();
        SystemSafetyConstraintInsertDto mock = assembleSystemSafetyConstraintInsertDto.get();
        mock.setAnalysisId(mockAnalysisId);

        when(microStampClient.getAnalysisById(mockAnalysisId)).thenThrow(FeignException.class);

        assertThrows(FeignException.class, () -> service.insert(mock));
    }

    @Test
    @DisplayName("#insert > When a hazard associated with the system safety constraint is not found > Throw an exception")
    void insertWhenAHazardAssociatedWithTheSystemSafetyConstraintIsNotFoundThrowAnException() {
        UUID mockAnalysisId = UUID.randomUUID();
        SystemSafetyConstraintInsertDto mock = assembleSystemSafetyConstraintInsertDto.get();
        mock.setAnalysisId(mockAnalysisId);

        when(microStampClient.getAnalysisById(mockAnalysisId)).thenReturn(null);
        when(hazardRepository.findById(mock.getHazardsId().getFirst())).thenReturn(Optional.empty());

        assertThrows(Step1NotFoundException.class, () -> service.insert(mock));
    }

    @Test
    @DisplayName("#insert > When the input are valid > Create the system safety constraint")
    void insertWhenTheInputAreValidCreateTheSystemSafetyConstraint() {
        UUID mockAnalysisId = UUID.randomUUID();
        SystemSafetyConstraintInsertDto mockSystemSafetyConstraintInsertDto = assembleSystemSafetyConstraintInsertDto.get();
        mockSystemSafetyConstraintInsertDto.setAnalysisId(mockAnalysisId);

        when(microStampClient.getAnalysisById(mockSystemSafetyConstraintInsertDto.getAnalysisId())).thenReturn(null);
        when(repository.save(any(SystemSafetyConstraint.class))).thenAnswer(answer -> answer.getArguments()[0]);
        when(hazardRepository.findById(mockSystemSafetyConstraintInsertDto.getHazardsId().getFirst())).thenReturn(Optional.ofNullable(assembleHazard.apply(mockSystemSafetyConstraintInsertDto.getHazardsId().getFirst())));
        when(hazardRepository.findById(mockSystemSafetyConstraintInsertDto.getHazardsId().get(1))).thenReturn(Optional.ofNullable(assembleHazard.apply(mockSystemSafetyConstraintInsertDto.getHazardsId().get(1))));

        SystemSafetyConstraintReadDto response = service.insert(mockSystemSafetyConstraintInsertDto);
        assertAll(
                () -> assertEquals(mockSystemSafetyConstraintInsertDto.getName(), response.getName()),
                () -> assertEquals(mockSystemSafetyConstraintInsertDto.getCode(), response.getCode())
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
        UUID mockAnalysisId = UUID.randomUUID();
        when(repository.findById(mockAnalysisId)).thenReturn(Optional.empty());

        assertThrows(Step1NotFoundException.class, () -> service.update(mockAnalysisId, assembleSystemSafetyConstraintUpdateDto.get()));
    }

    @Test
    @DisplayName("#update > When the analysis exist > Update the system safety constraint")
    void updateWhenTheAnalysisExistUpdateTheSystemSafetyConstraint() {
        UUID mockSystemSafetyConstraintId = UUID.randomUUID();
        SystemSafetyConstraint mockSystemSafetyConstraint = assembleSystemSafetyConstraint.apply(1);
        SystemSafetyConstraintUpdateDto mockSystemSafetyConstraintIdUpdate = assembleSystemSafetyConstraintUpdateDto.get();
        when(repository.findById(mockSystemSafetyConstraintId)).thenReturn(Optional.of(mockSystemSafetyConstraint));
        mockSystemSafetyConstraint.setName(mockSystemSafetyConstraintIdUpdate.getName());

        service.update(mockSystemSafetyConstraintId, mockSystemSafetyConstraintIdUpdate);

        assertAll(
                () -> verify(repository, times(1)).findById(mockSystemSafetyConstraintId),
                () -> verify(repository, times(1)).save(mockSystemSafetyConstraint)
        );
    }

    @Test
    @DisplayName("#delete > When no system safety constraint is found > Throw an exception")
    void deleteWhenNoSystemSafetyConstraintIsFoundThrowAnException() {
        UUID mockSystemSafetyConstraintId = UUID.randomUUID();
        when(repository.findById(mockSystemSafetyConstraintId)).thenReturn(Optional.empty());

        assertThrows(Step1NotFoundException.class, () -> service.delete(mockSystemSafetyConstraintId));
    }

    @Test
    @DisplayName("#delete > When the system safety constraint is found > Delete the system safety constraint and hazards")
    void deleteWhenTheSystemSafetyConstraintIsFoundDeleteTheSystemSafetyConstraintAndHazards() {
        UUID mockSystemSafetyConstraintId = UUID.randomUUID();
        SystemSafetyConstraint mockSystemSafetyConstraint = assembleSystemSafetyConstraint.apply(1);
        mockSystemSafetyConstraint.setId(mockSystemSafetyConstraintId);
        when(repository.findById(mockSystemSafetyConstraintId)).thenReturn(Optional.of(mockSystemSafetyConstraint));

        service.delete(mockSystemSafetyConstraintId);

        assertAll(
                () -> verify(repository, times(1)).findById(mockSystemSafetyConstraintId),
                () -> verify(repository, times(1)).deleteById(mockSystemSafetyConstraintId)
        );
    }

    private final Function<Integer, SystemSafetyConstraint> assembleSystemSafetyConstraint = (index) -> SystemSafetyConstraint.builder()
            .id(UUID.randomUUID())
            .name("Name " + index)
            .code("Code " + index)
            .build();

    private final Supplier<SystemSafetyConstraintInsertDto> assembleSystemSafetyConstraintInsertDto = () -> SystemSafetyConstraintInsertDto.builder()
            .name("Insert")
            .code("Code")
            .analysisId(UUID.randomUUID())
            .hazardsId(List.of(UUID.randomUUID(),UUID.randomUUID()))
            .build();

    private final Supplier<SystemSafetyConstraintUpdateDto> assembleSystemSafetyConstraintUpdateDto = () -> SystemSafetyConstraintUpdateDto.builder()
            .name("Insert")
            .code("Code")
            .build();

    private final Function<UUID, Hazard> assembleHazard = (index) -> Hazard.builder()
            .id(UUID.randomUUID())
            .name("Insert " + index)
            .code("Code" + index)
            .build();

}
