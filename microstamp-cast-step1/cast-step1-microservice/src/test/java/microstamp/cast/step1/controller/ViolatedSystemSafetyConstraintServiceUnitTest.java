package microstamp.cast.step1.controller;

import microstamp.cast.step1.client.MicroStampClient;
import microstamp.cast.step1.dto.violatedsystemsafetyconstraint.ViolatedSystemSafetyConstraintInsertDto;
import microstamp.cast.step1.dto.violatedsystemsafetyconstraint.ViolatedSystemSafetyConstraintReadDto;
import microstamp.cast.step1.dto.violatedsystemsafetyconstraint.ViolatedSystemSafetyConstraintUpdateDto;
import microstamp.cast.step1.entity.CastHazard;
import microstamp.cast.step1.entity.ViolatedSystemSafetyConstraint;
import microstamp.cast.step1.exception.Step1IllegalArgumentException;
import microstamp.cast.step1.exception.Step1NotFoundException;
import microstamp.cast.step1.repository.CastHazardRepository;
import microstamp.cast.step1.repository.ViolatedSystemSafetyConstraintRepository;
import microstamp.cast.step1.service.impl.ViolatedSystemSafetyConstraintServiceImpl;
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
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ViolatedSystemSafetyConstraintServiceUnitTest {

    @InjectMocks
    private ViolatedSystemSafetyConstraintServiceImpl service;

    @Mock
    private ViolatedSystemSafetyConstraintRepository violatedSystemSafetyConstraintRepository;

    @Mock
    private CastHazardRepository castHazardRepository;

    @Mock
    private MicroStampClient microStampClient;

    @Test
    @DisplayName("#findAll > When no violated constraint is found > Return an empty list")
    void findAllWhenNoViolatedConstraintIsFoundReturnAnEmptyList() {
        when(violatedSystemSafetyConstraintRepository.findAll()).thenReturn(List.of());

        List<ViolatedSystemSafetyConstraintReadDto> response = service.findAll();

        assertTrue(response.isEmpty());
    }

    @Test
    @DisplayName("#findAll > When there are violated constraints found > Return them sorted by code")
    void findAllWhenThereAreViolatedConstraintsFoundReturnThemSortedByCode() {
        when(violatedSystemSafetyConstraintRepository.findAll()).thenReturn(List.of(
                assembleViolatedConstraint.apply(2),
                assembleViolatedConstraint.apply(1)
        ));

        List<ViolatedSystemSafetyConstraintReadDto> response = service.findAll();

        assertAll(
                () -> Assertions.assertFalse(response.isEmpty()),
                () -> assertEquals(2, response.size()),
                () -> assertEquals("VSSC-1", response.getFirst().getCode()),
                () -> assertEquals("VSSC-2", response.get(1).getCode())
        );
    }

    @Test
    @DisplayName("#findById > When no violated constraint is found > Throw an exception")
    void findByIdWhenNoViolatedConstraintIsFoundThrowAnException() {
        UUID mockId = UUID.randomUUID();

        when(violatedSystemSafetyConstraintRepository.findById(mockId)).thenReturn(Optional.empty());

        assertThrows(Step1NotFoundException.class, () -> service.findById(mockId));
    }

    @Test
    @DisplayName("#findById > When violated constraint is found > Return it")
    void findByIdWhenViolatedConstraintIsFoundReturnIt() {
        UUID mockId = UUID.randomUUID();
        ViolatedSystemSafetyConstraint mock = assembleViolatedConstraint.apply(1);
        mock.setId(mockId);

        when(violatedSystemSafetyConstraintRepository.findById(mockId)).thenReturn(Optional.of(mock));

        ViolatedSystemSafetyConstraintReadDto response = service.findById(mockId);

        assertAll(
                () -> assertEquals(mock.getId(), response.getId()),
                () -> assertEquals(mock.getCode(), response.getCode()),
                () -> assertEquals(mock.getName(), response.getName()),
                () -> assertEquals(mock.getDescription(), response.getDescription())
        );
    }

    @Test
    @DisplayName("#findByAnalysisId > When no violated constraint is found > Return an empty list")
    void findByAnalysisIdWhenNoViolatedConstraintIsFoundReturnAnEmptyList() {
        UUID mockAnalysisId = UUID.randomUUID();

        when(violatedSystemSafetyConstraintRepository.findByAnalysisId(mockAnalysisId)).thenReturn(List.of());

        List<ViolatedSystemSafetyConstraintReadDto> response = service.findByAnalysisId(mockAnalysisId);

        assertTrue(response.isEmpty());
    }

    @Test
    @DisplayName("#findByAnalysisId > When there are violated constraints found > Return them")
    void findByAnalysisIdWhenThereAreViolatedConstraintsFoundReturnThem() {
        UUID mockAnalysisId = UUID.randomUUID();

        when(violatedSystemSafetyConstraintRepository.findByAnalysisId(mockAnalysisId)).thenReturn(List.of(
                assembleViolatedConstraint.apply(1),
                assembleViolatedConstraint.apply(2)
        ));

        List<ViolatedSystemSafetyConstraintReadDto> response = service.findByAnalysisId(mockAnalysisId);

        assertAll(
                () -> Assertions.assertFalse(response.isEmpty()),
                () -> assertEquals(2, response.size()),
                () -> assertEquals("VSSC-1", response.getFirst().getCode()),
                () -> assertEquals("VSSC-2", response.get(1).getCode())
        );
    }

    @Test
    @DisplayName("#insert > When input is invalid > Throw an exception")
    void insertWhenInputIsInvalidThrowAnException() {
        assertThrows(Step1IllegalArgumentException.class, () -> service.insert(null));
    }

    @Test
    @DisplayName("#insert > When associated CAST hazard is not found > Throw an exception")
    void insertWhenAssociatedCastHazardIsNotFoundThrowAnException() {
        ViolatedSystemSafetyConstraintInsertDto insertDto = assembleViolatedConstraintInsertDto.get();

        when(microStampClient.getAnalysisById(insertDto.getAnalysisId())).thenReturn(null);
        when(castHazardRepository.findById(insertDto.getHazardIds().getFirst())).thenReturn(Optional.empty());

        assertThrows(Step1NotFoundException.class, () -> service.insert(insertDto));
    }

    @Test
    @DisplayName("#insert > When input is valid > Create violated constraint with hazards")
    void insertWhenInputIsValidCreateViolatedConstraintWithHazards() {
        ViolatedSystemSafetyConstraintInsertDto insertDto = assembleViolatedConstraintInsertDto.get();
        UUID firstHazardId = insertDto.getHazardIds().getFirst();
        UUID secondHazardId = insertDto.getHazardIds().get(1);

        when(microStampClient.getAnalysisById(insertDto.getAnalysisId())).thenReturn(null);
        when(castHazardRepository.findById(firstHazardId)).thenReturn(Optional.of(assembleCastHazard.apply(firstHazardId, 1)));
        when(castHazardRepository.findById(secondHazardId)).thenReturn(Optional.of(assembleCastHazard.apply(secondHazardId, 2)));
        when(violatedSystemSafetyConstraintRepository.save(any(ViolatedSystemSafetyConstraint.class))).thenAnswer(answer -> answer.getArguments()[0]);

        ViolatedSystemSafetyConstraintReadDto response = service.insert(insertDto);

        assertAll(
                () -> assertEquals(insertDto.getCode(), response.getCode()),
                () -> assertEquals(insertDto.getName(), response.getName()),
                () -> assertEquals(insertDto.getDescription(), response.getDescription()),
                () -> assertEquals(2, response.getHazards().size()),
                () -> verify(violatedSystemSafetyConstraintRepository, times(1)).save(any(ViolatedSystemSafetyConstraint.class))
        );
    }

    @Test
    @DisplayName("#insert > When hazard ids are null > Create violated constraint with empty hazards list")
    void insertWhenHazardIdsAreNullCreateViolatedConstraintWithEmptyHazardsList() {
        ViolatedSystemSafetyConstraintInsertDto insertDto = assembleViolatedConstraintInsertDto.get();
        insertDto.setHazardIds(null);

        when(microStampClient.getAnalysisById(insertDto.getAnalysisId())).thenReturn(null);
        when(violatedSystemSafetyConstraintRepository.save(any(ViolatedSystemSafetyConstraint.class))).thenAnswer(answer -> answer.getArguments()[0]);

        ViolatedSystemSafetyConstraintReadDto response = service.insert(insertDto);

        assertAll(
                () -> assertEquals(insertDto.getCode(), response.getCode()),
                () -> assertTrue(response.getHazards().isEmpty()),
                () -> verify(castHazardRepository, never()).findById(any(UUID.class)),
                () -> verify(violatedSystemSafetyConstraintRepository, times(1)).save(any(ViolatedSystemSafetyConstraint.class))
        );
    }

    @Test
    @DisplayName("#update > When input is invalid > Throw an exception")
    void updateWhenInputIsInvalidThrowAnException() {
        assertThrows(Step1IllegalArgumentException.class, () -> service.update(UUID.randomUUID(), null));
    }

    @Test
    @DisplayName("#update > When violated constraint does not exist > Throw an exception")
    void updateWhenViolatedConstraintDoesNotExistThrowAnException() {
        UUID mockId = UUID.randomUUID();

        when(violatedSystemSafetyConstraintRepository.findById(mockId)).thenReturn(Optional.empty());

        assertThrows(Step1NotFoundException.class, () -> service.update(mockId, assembleViolatedConstraintUpdateDto.get()));
    }

    @Test
    @DisplayName("#update > When associated CAST hazard is not found > Throw an exception")
    void updateWhenAssociatedCastHazardIsNotFoundThrowAnException() {
        UUID mockId = UUID.randomUUID();
        ViolatedSystemSafetyConstraint mock = assembleViolatedConstraint.apply(1);
        ViolatedSystemSafetyConstraintUpdateDto updateDto = assembleViolatedConstraintUpdateDto.get();

        when(violatedSystemSafetyConstraintRepository.findById(mockId)).thenReturn(Optional.of(mock));
        when(castHazardRepository.findById(updateDto.getHazardIds().getFirst())).thenReturn(Optional.empty());

        assertThrows(Step1NotFoundException.class, () -> service.update(mockId, updateDto));
    }

    @Test
    @DisplayName("#update > When input is valid > Update violated constraint")
    void updateWhenInputIsValidUpdateViolatedConstraint() {
        UUID mockId = UUID.randomUUID();
        ViolatedSystemSafetyConstraint mock = assembleViolatedConstraint.apply(1);
        ViolatedSystemSafetyConstraintUpdateDto updateDto = assembleViolatedConstraintUpdateDto.get();

        UUID firstHazardId = updateDto.getHazardIds().getFirst();
        UUID secondHazardId = updateDto.getHazardIds().get(1);

        when(violatedSystemSafetyConstraintRepository.findById(mockId)).thenReturn(Optional.of(mock));
        when(castHazardRepository.findById(firstHazardId)).thenReturn(Optional.of(assembleCastHazard.apply(firstHazardId, 1)));
        when(castHazardRepository.findById(secondHazardId)).thenReturn(Optional.of(assembleCastHazard.apply(secondHazardId, 2)));

        service.update(mockId, updateDto);

        assertAll(
                () -> assertEquals(updateDto.getCode(), mock.getCode()),
                () -> assertEquals(updateDto.getName(), mock.getName()),
                () -> assertEquals(updateDto.getDescription(), mock.getDescription()),
                () -> assertEquals(2, mock.getHazards().size()),
                () -> verify(violatedSystemSafetyConstraintRepository, times(1)).save(mock)
        );
    }

    @Test
    @DisplayName("#delete > When violated constraint does not exist > Throw an exception")
    void deleteWhenViolatedConstraintDoesNotExistThrowAnException() {
        UUID mockId = UUID.randomUUID();

        when(violatedSystemSafetyConstraintRepository.findById(mockId)).thenReturn(Optional.empty());

        assertThrows(Step1NotFoundException.class, () -> service.delete(mockId));
    }

    @Test
    @DisplayName("#delete > When violated constraint does exist > Delete it")
    void deleteWhenViolatedConstraintExistsDeleteIt() {
        UUID mockId = UUID.randomUUID();
        ViolatedSystemSafetyConstraint mock = assembleViolatedConstraint.apply(1);
        mock.setId(mockId);

        when(violatedSystemSafetyConstraintRepository.findById(mockId)).thenReturn(Optional.of(mock));

        service.delete(mockId);

        assertAll(
                () -> verify(violatedSystemSafetyConstraintRepository, times(1)).findById(mockId),
                () -> verify(violatedSystemSafetyConstraintRepository, times(1)).deleteHazardAssociation(mockId.toString()),

                () -> verify(violatedSystemSafetyConstraintRepository, times(1)).deleteById(mockId)
        );
    }

    private final Function<Integer, ViolatedSystemSafetyConstraint> assembleViolatedConstraint = index -> ViolatedSystemSafetyConstraint.builder()
            .id(UUID.randomUUID())
            .code("VSSC-" + index)
            .name("Violated Constraint " + index)
            .description("Description " + index)
            .hazards(List.of())
            .analysisId(UUID.randomUUID())
            .build();

    private final BiFunction<UUID, Integer, CastHazard> assembleCastHazard = (uuid, index) -> CastHazard.builder()
            .id(uuid)
            .code("CH-" + index)
            .name("CAST Hazard " + index)
            .description("Description " + index)
            .analysisId(UUID.randomUUID())
            .build();

    private final Supplier<ViolatedSystemSafetyConstraintInsertDto> assembleViolatedConstraintInsertDto = () -> ViolatedSystemSafetyConstraintInsertDto.builder()
            .code("VSSC-1")
            .name("Violated Constraint")
            .description("Description")
            .hazardIds(List.of(UUID.randomUUID(), UUID.randomUUID()))
            .analysisId(UUID.randomUUID())
            .build();

    private final Supplier<ViolatedSystemSafetyConstraintUpdateDto> assembleViolatedConstraintUpdateDto = () -> ViolatedSystemSafetyConstraintUpdateDto.builder()
            .code("VSSC-U")
            .name("Updated Violated Constraint")
            .description("Updated description")
            .hazardIds(List.of(UUID.randomUUID(), UUID.randomUUID()))
            .build();
}