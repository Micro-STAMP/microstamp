package microstamp.step4.unit;

import microstamp.step3.dto.unsafecontrolaction.UnsafeControlActionReadDto;
import microstamp.step4.client.MicroStampAuthClient;
import microstamp.step4.client.MicroStampStep3Client;
import microstamp.step4.dto.fourtuple.FourTupleFullReadDto;
import microstamp.step4.dto.fourtuple.FourTupleInsertDto;
import microstamp.step4.dto.fourtuple.FourTupleUpdateDto;
import microstamp.step4.entity.FourTuple;
import microstamp.step4.exception.Step4IllegalArgumentException;
import microstamp.step4.exception.Step4NotFoundException;
import microstamp.step4.repository.FourTupleRepository;
import microstamp.step4.service.impl.FourTupleServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FourTupleServiceUnitTest {

    @InjectMocks
    private FourTupleServiceImpl service;

    @Mock
    private FourTupleRepository fourTupleRepository;

    @Mock
    private MicroStampAuthClient microStampAuthClient;

    @Mock
    private MicroStampStep3Client microStampStep3Client;

    @Test
    @DisplayName("#findAll > When no 4-tuples are found > Return an empty list")
    void findAllWhenNoFourTuplesAreFoundReturnEmptyList() {
        when(fourTupleRepository.findAll()).thenReturn(List.of());

        List<FourTupleFullReadDto> response = service.findAll();

        assertTrue(response.isEmpty());
    }

    @Test
    @DisplayName("#findById > When the 4-tuple is not found > Throw an exception")
    void findByIdWhenNotFoundThrowException() {
        UUID id = UUID.randomUUID();
        when(fourTupleRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(Step4NotFoundException.class, () -> service.findById(id));
    }

    @Test
    @DisplayName("#insert > When the input is null > Throw an exception")
    void insertWhenInputIsNullThrowException() {
        assertThrows(Step4IllegalArgumentException.class, () -> service.insert(null));
    }

    @Test
    @DisplayName("#insert > When the input is valid > Save and return the created 4-tuple")
    void insertWhenInputIsValidSavesAndReturnsDto() {
        UUID analysisId = UUID.randomUUID();
        UUID ucaId1 = UUID.randomUUID();
        UUID ucaId2 = UUID.randomUUID();

        FourTupleInsertDto insertDto = FourTupleInsertDto.builder()
                .analysisId(analysisId)
                .code("FT-1")
                .scenario("Scenario")
                .associatedCausalFactor("ACF")
                .recommendation("Rec")
                .rationale("Rat")
                .unsafeControlActionIds(List.of(ucaId1, ucaId2))
                .build();

        when(microStampAuthClient.getAnalysisById(analysisId)).thenReturn(null);
        when(microStampStep3Client.readUnsafeControlAction(ucaId1)).thenReturn(assembleUcaReadDto.apply(ucaId1));
        when(microStampStep3Client.readUnsafeControlAction(ucaId2)).thenReturn(assembleUcaReadDto.apply(ucaId2));

        when(fourTupleRepository.save(any(FourTuple.class))).thenAnswer(invocation -> {
            FourTuple ft = invocation.getArgument(0);
            if (ft.getId() == null) ft.setId(UUID.randomUUID());
            return ft;
        });

        FourTupleFullReadDto response = service.insert(insertDto);

        assertAll(
                () -> assertEquals(insertDto.getCode(), response.getCode()),
                () -> assertEquals(insertDto.getScenario(), response.getScenario()),
                () -> assertEquals(2, response.getUnsafeControlActions().size())
        );

        verify(fourTupleRepository, times(1)).save(any(FourTuple.class));
        verify(microStampAuthClient, times(1)).getAnalysisById(analysisId);
        verify(microStampStep3Client, times(1)).readUnsafeControlAction(ucaId1);
        verify(microStampStep3Client, times(1)).readUnsafeControlAction(ucaId2);
    }

    @Test
    @DisplayName("#update > When the 4-tuple is not found > Throw an exception")
    void updateWhenNotFoundThrowException() {
        UUID id = UUID.randomUUID();
        when(fourTupleRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(Step4NotFoundException.class, () -> service.update(id, FourTupleUpdateDto.builder()
                .code("FT-1").scenario("S").associatedCausalFactor("A").recommendation("R").rationale("Ra")
                .unsafeControlActionIds(new ArrayList<>()).build()));
    }

    @Test
    @DisplayName("#delete > When the 4-tuple is not found > Throw an exception")
    void deleteWhenNotFoundThrowException() {
        UUID id = UUID.randomUUID();
        when(fourTupleRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(Step4NotFoundException.class, () -> service.delete(id));
    }

    @Test
    @DisplayName("#findByAnalysisIdSortedByUnsafeControlActions > When UCAs and 4-tuples are found > Group 4-tuples by UCA")
    void findByAnalysisIdSortedByUnsafeControlActionsGroupsFourTuplesByUca() {
        UUID analysisId = UUID.randomUUID();
        UUID ucaId = UUID.randomUUID();

        FourTuple ft1 = assembleFourTuple("FT-2", analysisId, List.of(ucaId));
        FourTuple ft2 = assembleFourTuple("FT-1", analysisId, List.of(ucaId));

        when(fourTupleRepository.findByAnalysisId(analysisId)).thenReturn(List.of(ft1, ft2));
        when(microStampStep3Client.readAllUCAByAnalysisId(analysisId)).thenReturn(List.of(assembleUcaReadDto.apply(ucaId)));

        var response = service.findByAnalysisIdSortedByUnsafeControlActions(analysisId);

        assertAll(
                () -> assertEquals(1, response.size()),
                () -> assertEquals(2, response.getFirst().getFourTuples().size()),
                () -> assertEquals("FT-1", response.getFirst().getFourTuples().getFirst().getCode())
        );
    }

    @Test
    @DisplayName("#findByAnalysisId(paged) > When 4-tuples are found > Return page of mapped DTOs")
    void findByAnalysisIdPagedReturnPage() {
        UUID analysisId = UUID.randomUUID();
        FourTuple ft1 = assembleFourTuple("FT-1", analysisId, List.of());
        FourTuple ft2 = assembleFourTuple("FT-2", analysisId, List.of());
        Page<FourTuple> page = new PageImpl<>(List.of(ft1, ft2));

        when(fourTupleRepository.findByAnalysisIdOrderByCode(eq(analysisId), any(Pageable.class))).thenReturn(page);

        Page<FourTupleFullReadDto> response = service.findByAnalysisId(analysisId, 0, 10);

        assertAll(
                () -> assertEquals(2, response.getContent().size()),
                () -> assertEquals("FT-1", response.getContent().getFirst().getCode())
        );
    }

    private FourTuple assembleFourTuple(String code, UUID analysisId, List<UUID> ucaIds) {
        return FourTuple.builder()
                .id(UUID.randomUUID())
                .code(code)
                .scenario("Scenario")
                .associatedCausalFactor("ACF")
                .recommendation("Rec")
                .rationale("Rat")
                .unsafeControlActions(new ArrayList<>(ucaIds))
                .analysisId(analysisId)
                .build();
    }

    private final Function<UUID, UnsafeControlActionReadDto> assembleUcaReadDto = (UUID mockUcaId) -> UnsafeControlActionReadDto.builder()
            .id(mockUcaId)
            .analysis_id(UUID.randomUUID())
            .name("UCA")
            .uca_code("U-1")
            .build();
}
