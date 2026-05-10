package microstamp.cast.step1.unit;

import microstamp.cast.step1.client.MicroStampClient;
import microstamp.cast.step1.dto.physicallossanalysis.PhysicalLossAnalysisInsertDto;
import microstamp.cast.step1.dto.physicallossanalysis.PhysicalLossAnalysisReadDto;
import microstamp.cast.step1.dto.physicallossanalysis.PhysicalLossAnalysisUpdateDto;
import microstamp.cast.step1.entity.PhysicalLossAnalysis;
import microstamp.cast.step1.exception.Step1IllegalArgumentException;
import microstamp.cast.step1.exception.Step1NotFoundException;
import microstamp.cast.step1.repository.PhysicalLossAnalysisRepository;
import microstamp.cast.step1.service.impl.PhysicalLossAnalysisServiceImpl;
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
public class PhysicalLossAnalysisServiceUnitTest {

    @InjectMocks
    private PhysicalLossAnalysisServiceImpl service;

    @Mock
    private PhysicalLossAnalysisRepository physicalLossAnalysisRepository;

    @Mock
    private MicroStampClient microStampClient;

    @Test
    @DisplayName("#findAll > When no physical loss analysis is found > Return an empty list")
    void findAllWhenNoPhysicalLossAnalysisIsFoundReturnAnEmptyList() {
        when(physicalLossAnalysisRepository.findAll()).thenReturn(List.of());

        List<PhysicalLossAnalysisReadDto> response = service.findAll();

        assertTrue(response.isEmpty());
    }

    @Test
    @DisplayName("#findAll > When there are physical loss analyses found > Return them sorted by code")
    void findAllWhenThereArePhysicalLossAnalysesFoundReturnThemSortedByCode() {
        when(physicalLossAnalysisRepository.findAll()).thenReturn(List.of(
                assemblePhysicalLossAnalysis.apply(2),
                assemblePhysicalLossAnalysis.apply(1)
        ));

        List<PhysicalLossAnalysisReadDto> response = service.findAll();

        assertAll(
                () -> Assertions.assertFalse(response.isEmpty()),
                () -> assertEquals(2, response.size()),
                () -> assertEquals("PLA-1", response.getFirst().getCode()),
                () -> assertEquals("PLA-2", response.get(1).getCode())
        );
    }

    @Test
    @DisplayName("#findById > When no physical loss analysis is found > Throw an exception")
    void findByIdWhenNoPhysicalLossAnalysisIsFoundThrowAnException() {
        UUID mockId = UUID.randomUUID();

        when(physicalLossAnalysisRepository.findById(mockId)).thenReturn(Optional.empty());

        assertThrows(Step1NotFoundException.class, () -> service.findById(mockId));
    }

    @Test
    @DisplayName("#findById > When physical loss analysis is found > Return it")
    void findByIdWhenPhysicalLossAnalysisIsFoundReturnIt() {
        UUID mockId = UUID.randomUUID();
        PhysicalLossAnalysis mock = assemblePhysicalLossAnalysis.apply(1);
        mock.setId(mockId);

        when(physicalLossAnalysisRepository.findById(mockId)).thenReturn(Optional.of(mock));

        PhysicalLossAnalysisReadDto response = service.findById(mockId);

        assertAll(
                () -> assertEquals(mock.getId(), response.getId()),
                () -> assertEquals(mock.getCode(), response.getCode()),
                () -> assertEquals(mock.getPhysicalLossDescription(), response.getPhysicalLossDescription()),
                () -> assertEquals(mock.getAffectedEquipment(), response.getAffectedEquipment()),
                () -> assertEquals(mock.getPhysicalDesignRequirements(), response.getPhysicalDesignRequirements()),
                () -> assertEquals(mock.getPhysicalControls(), response.getPhysicalControls()),
                () -> assertEquals(mock.getFailuresAndUnsafeInteractions(), response.getFailuresAndUnsafeInteractions()),
                () -> assertEquals(mock.getMissingOrInadequateControls(), response.getMissingOrInadequateControls()),
                () -> assertEquals(mock.getContextualFactors(), response.getContextualFactors())
        );
    }

    @Test
    @DisplayName("#findByAnalysisId > When no physical loss analysis is found > Return an empty list")
    void findByAnalysisIdWhenNoPhysicalLossAnalysisIsFoundReturnAnEmptyList() {
        UUID mockAnalysisId = UUID.randomUUID();

        when(physicalLossAnalysisRepository.findByAnalysisId(mockAnalysisId)).thenReturn(List.of());

        List<PhysicalLossAnalysisReadDto> response = service.findByAnalysisId(mockAnalysisId);

        assertTrue(response.isEmpty());
    }

    @Test
    @DisplayName("#findByAnalysisId > When there are physical loss analyses found > Return them")
    void findByAnalysisIdWhenThereArePhysicalLossAnalysesFoundReturnThem() {
        UUID mockAnalysisId = UUID.randomUUID();

        when(physicalLossAnalysisRepository.findByAnalysisId(mockAnalysisId)).thenReturn(List.of(
                assemblePhysicalLossAnalysis.apply(1),
                assemblePhysicalLossAnalysis.apply(2)
        ));

        List<PhysicalLossAnalysisReadDto> response = service.findByAnalysisId(mockAnalysisId);

        assertAll(
                () -> Assertions.assertFalse(response.isEmpty()),
                () -> assertEquals(2, response.size()),
                () -> assertEquals("PLA-1", response.getFirst().getCode()),
                () -> assertEquals("PLA-2", response.get(1).getCode())
        );
    }

    @Test
    @DisplayName("#insert > When input is invalid > Throw an exception")
    void insertWhenInputIsInvalidThrowAnException() {
        assertThrows(Step1IllegalArgumentException.class, () -> service.insert(null));
    }

    @Test
    @DisplayName("#insert > When input is valid > Create physical loss analysis")
    void insertWhenInputIsValidCreatePhysicalLossAnalysis() {
        PhysicalLossAnalysisInsertDto insertDto = assemblePhysicalLossAnalysisInsertDto.get();

        when(microStampClient.getAnalysisById(insertDto.getAnalysisId())).thenReturn(null);
        when(physicalLossAnalysisRepository.save(any(PhysicalLossAnalysis.class))).thenAnswer(answer -> answer.getArguments()[0]);

        PhysicalLossAnalysisReadDto response = service.insert(insertDto);

        assertAll(
                () -> assertEquals(insertDto.getCode(), response.getCode()),
                () -> assertEquals(insertDto.getPhysicalLossDescription(), response.getPhysicalLossDescription()),
                () -> assertEquals(insertDto.getAffectedEquipment(), response.getAffectedEquipment()),
                () -> assertEquals(insertDto.getPhysicalDesignRequirements(), response.getPhysicalDesignRequirements()),
                () -> assertEquals(insertDto.getPhysicalControls(), response.getPhysicalControls()),
                () -> assertEquals(insertDto.getFailuresAndUnsafeInteractions(), response.getFailuresAndUnsafeInteractions()),
                () -> assertEquals(insertDto.getMissingOrInadequateControls(), response.getMissingOrInadequateControls()),
                () -> assertEquals(insertDto.getContextualFactors(), response.getContextualFactors()),
                () -> verify(microStampClient, times(1)).getAnalysisById(insertDto.getAnalysisId()),
                () -> verify(physicalLossAnalysisRepository, times(1)).save(any(PhysicalLossAnalysis.class))
        );
    }

    @Test
    @DisplayName("#update > When input is invalid > Throw an exception")
    void updateWhenInputIsInvalidThrowAnException() {
        assertThrows(Step1IllegalArgumentException.class, () -> service.update(UUID.randomUUID(), null));
    }

    @Test
    @DisplayName("#update > When physical loss analysis does not exist > Throw an exception")
    void updateWhenPhysicalLossAnalysisDoesNotExistThrowAnException() {
        UUID mockId = UUID.randomUUID();

        when(physicalLossAnalysisRepository.findById(mockId)).thenReturn(Optional.empty());

        assertThrows(Step1NotFoundException.class, () -> service.update(mockId, assemblePhysicalLossAnalysisUpdateDto.get()));
    }

    @Test
    @DisplayName("#update > When physical loss analysis exists > Update it")
    void updateWhenPhysicalLossAnalysisExistsUpdateIt() {
        UUID mockId = UUID.randomUUID();
        PhysicalLossAnalysis mock = assemblePhysicalLossAnalysis.apply(1);
        PhysicalLossAnalysisUpdateDto updateDto = assemblePhysicalLossAnalysisUpdateDto.get();

        when(physicalLossAnalysisRepository.findById(mockId)).thenReturn(Optional.of(mock));

        service.update(mockId, updateDto);

        assertAll(
                () -> assertEquals(updateDto.getCode(), mock.getCode()),
                () -> assertEquals(updateDto.getPhysicalLossDescription(), mock.getPhysicalLossDescription()),
                () -> assertEquals(updateDto.getAffectedEquipment(), mock.getAffectedEquipment()),
                () -> assertEquals(updateDto.getPhysicalDesignRequirements(), mock.getPhysicalDesignRequirements()),
                () -> assertEquals(updateDto.getPhysicalControls(), mock.getPhysicalControls()),
                () -> assertEquals(updateDto.getFailuresAndUnsafeInteractions(), mock.getFailuresAndUnsafeInteractions()),
                () -> assertEquals(updateDto.getMissingOrInadequateControls(), mock.getMissingOrInadequateControls()),
                () -> assertEquals(updateDto.getContextualFactors(), mock.getContextualFactors()),
                () -> verify(physicalLossAnalysisRepository, times(1)).save(mock)
        );
    }

    @Test
    @DisplayName("#delete > When physical loss analysis does not exist > Throw an exception")
    void deleteWhenPhysicalLossAnalysisDoesNotExistThrowAnException() {
        UUID mockId = UUID.randomUUID();

        when(physicalLossAnalysisRepository.findById(mockId)).thenReturn(Optional.empty());

        assertThrows(Step1NotFoundException.class, () -> service.delete(mockId));
    }

    @Test
    @DisplayName("#delete > When physical loss analysis exists > Delete it")
    void deleteWhenPhysicalLossAnalysisExistsDeleteIt() {
        UUID mockId = UUID.randomUUID();
        PhysicalLossAnalysis mock = assemblePhysicalLossAnalysis.apply(1);
        mock.setId(mockId);

        when(physicalLossAnalysisRepository.findById(mockId)).thenReturn(Optional.of(mock));

        service.delete(mockId);

        assertAll(
                () -> verify(physicalLossAnalysisRepository, times(1)).findById(mockId),
                () -> verify(physicalLossAnalysisRepository, times(1)).deleteById(mockId)
        );
    }

    private final Function<Integer, PhysicalLossAnalysis> assemblePhysicalLossAnalysis = index -> PhysicalLossAnalysis.builder()
            .id(UUID.randomUUID())
            .code("PLA-" + index)
            .physicalLossDescription("Physical loss description " + index)
            .affectedEquipment("Affected equipment " + index)
            .physicalDesignRequirements("Physical design requirements " + index)
            .physicalControls("Physical controls " + index)
            .failuresAndUnsafeInteractions("Failures and unsafe interactions " + index)
            .missingOrInadequateControls("Missing or inadequate controls " + index)
            .contextualFactors("Contextual factors " + index)
            .analysisId(UUID.randomUUID())
            .build();

    private final Supplier<PhysicalLossAnalysisInsertDto> assemblePhysicalLossAnalysisInsertDto = () -> PhysicalLossAnalysisInsertDto.builder()
            .code("PLA-1")
            .physicalLossDescription("Physical loss description")
            .affectedEquipment("Affected equipment")
            .physicalDesignRequirements("Physical design requirements")
            .physicalControls("Physical controls")
            .failuresAndUnsafeInteractions("Failures and unsafe interactions")
            .missingOrInadequateControls("Missing or inadequate controls")
            .contextualFactors("Contextual factors")
            .analysisId(UUID.randomUUID())
            .build();

    private final Supplier<PhysicalLossAnalysisUpdateDto> assemblePhysicalLossAnalysisUpdateDto = () -> PhysicalLossAnalysisUpdateDto.builder()
            .code("PLA-U")
            .physicalLossDescription("Updated physical loss description")
            .affectedEquipment("Updated affected equipment")
            .physicalDesignRequirements("Updated physical design requirements")
            .physicalControls("Updated physical controls")
            .failuresAndUnsafeInteractions("Updated failures and unsafe interactions")
            .missingOrInadequateControls("Updated missing or inadequate controls")
            .contextualFactors("Updated contextual factors")
            .build();
}