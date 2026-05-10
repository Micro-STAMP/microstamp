package microstamp.cast.step1.unit;

import microstamp.cast.step1.client.MicroStampClient;
import microstamp.cast.step1.dto.casthazard.CastHazardInsertDto;
import microstamp.cast.step1.dto.casthazard.CastHazardReadDto;
import microstamp.cast.step1.dto.casthazard.CastHazardUpdateDto;
import microstamp.cast.step1.entity.AccidentLossEvent;
import microstamp.cast.step1.entity.CastHazard;
import microstamp.cast.step1.exception.Step1IllegalArgumentException;
import microstamp.cast.step1.exception.Step1NotFoundException;
import microstamp.cast.step1.repository.AccidentLossEventRepository;
import microstamp.cast.step1.repository.CastHazardRepository;
import microstamp.cast.step1.service.impl.CastHazardServiceImpl;
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
public class CastHazardServiceUnitTest {

    @InjectMocks
    private CastHazardServiceImpl service;

    @Mock
    private CastHazardRepository castHazardRepository;

    @Mock
    private AccidentLossEventRepository accidentLossEventRepository;

    @Mock
    private MicroStampClient microStampClient;

    @Test
    @DisplayName("#findAll > When no CAST hazard is found > Return an empty list")
    void findAllWhenNoCastHazardIsFoundReturnAnEmptyList() {
        when(castHazardRepository.findAll()).thenReturn(List.of());

        List<CastHazardReadDto> response = service.findAll();

        assertTrue(response.isEmpty());
    }

    @Test
    @DisplayName("#findAll > When there are CAST hazards found > Return them sorted by code")
    void findAllWhenThereAreCastHazardsFoundReturnThemSortedByCode() {
        when(castHazardRepository.findAll()).thenReturn(List.of(
                assembleCastHazard.apply(2),
                assembleCastHazard.apply(1)
        ));

        List<CastHazardReadDto> response = service.findAll();

        assertAll(
                () -> Assertions.assertFalse(response.isEmpty()),
                () -> assertEquals(2, response.size()),
                () -> assertEquals("CH-1", response.getFirst().getCode()),
                () -> assertEquals("CH-2", response.get(1).getCode())
        );
    }

    @Test
    @DisplayName("#findById > When no CAST hazard is found > Throw an exception")
    void findByIdWhenNoCastHazardIsFoundThrowAnException() {
        UUID mockId = UUID.randomUUID();

        when(castHazardRepository.findById(mockId)).thenReturn(Optional.empty());

        assertThrows(Step1NotFoundException.class, () -> service.findById(mockId));
    }

    @Test
    @DisplayName("#findById > When CAST hazard is found > Return it")
    void findByIdWhenCastHazardIsFoundReturnIt() {
        UUID mockId = UUID.randomUUID();
        CastHazard mock = assembleCastHazard.apply(1);
        mock.setId(mockId);

        when(castHazardRepository.findById(mockId)).thenReturn(Optional.of(mock));

        CastHazardReadDto response = service.findById(mockId);

        assertAll(
                () -> assertEquals(mock.getId(), response.getId()),
                () -> assertEquals(mock.getCode(), response.getCode()),
                () -> assertEquals(mock.getName(), response.getName()),
                () -> assertEquals(mock.getDescription(), response.getDescription())
        );
    }

    @Test
    @DisplayName("#findByAnalysisId > When no CAST hazard is found > Return an empty list")
    void findByAnalysisIdWhenNoCastHazardIsFoundReturnAnEmptyList() {
        UUID mockAnalysisId = UUID.randomUUID();

        when(castHazardRepository.findByAnalysisId(mockAnalysisId)).thenReturn(List.of());

        List<CastHazardReadDto> response = service.findByAnalysisId(mockAnalysisId);

        assertTrue(response.isEmpty());
    }

    @Test
    @DisplayName("#findByAnalysisId > When there are CAST hazards found > Return them")
    void findByAnalysisIdWhenThereAreCastHazardsFoundReturnThem() {
        UUID mockAnalysisId = UUID.randomUUID();

        when(castHazardRepository.findByAnalysisId(mockAnalysisId)).thenReturn(List.of(
                assembleCastHazard.apply(1),
                assembleCastHazard.apply(2)
        ));

        List<CastHazardReadDto> response = service.findByAnalysisId(mockAnalysisId);

        assertAll(
                () -> Assertions.assertFalse(response.isEmpty()),
                () -> assertEquals(2, response.size()),
                () -> assertEquals("CH-1", response.getFirst().getCode()),
                () -> assertEquals("CH-2", response.get(1).getCode())
        );
    }

    @Test
    @DisplayName("#insert > When input is invalid > Throw an exception")
    void insertWhenInputIsInvalidThrowAnException() {
        assertThrows(Step1IllegalArgumentException.class, () -> service.insert(null));
    }

    @Test
    @DisplayName("#insert > When associated accident/loss event is not found > Throw an exception")
    void insertWhenAssociatedAccidentLossEventIsNotFoundThrowAnException() {
        CastHazardInsertDto insertDto = assembleCastHazardInsertDto.get();

        when(microStampClient.getAnalysisById(insertDto.getAnalysisId())).thenReturn(null);
        when(accidentLossEventRepository.findById(insertDto.getAccidentLossEventIds().getFirst())).thenReturn(Optional.empty());

        assertThrows(Step1NotFoundException.class, () -> service.insert(insertDto));
    }

    @Test
    @DisplayName("#insert > When input is valid > Create CAST hazard with associated accident/loss events")
    void insertWhenInputIsValidCreateCastHazardWithAssociatedAccidentLossEvents() {
        CastHazardInsertDto insertDto = assembleCastHazardInsertDto.get();
        UUID firstEventId = insertDto.getAccidentLossEventIds().getFirst();
        UUID secondEventId = insertDto.getAccidentLossEventIds().get(1);

        when(microStampClient.getAnalysisById(insertDto.getAnalysisId())).thenReturn(null);
        when(accidentLossEventRepository.findById(firstEventId)).thenReturn(Optional.of(assembleAccidentLossEvent.apply(firstEventId, 1)));
        when(accidentLossEventRepository.findById(secondEventId)).thenReturn(Optional.of(assembleAccidentLossEvent.apply(secondEventId, 2)));
        when(castHazardRepository.save(any(CastHazard.class))).thenAnswer(answer -> answer.getArguments()[0]);

        CastHazardReadDto response = service.insert(insertDto);

        assertAll(
                () -> assertEquals(insertDto.getCode(), response.getCode()),
                () -> assertEquals(insertDto.getName(), response.getName()),
                () -> assertEquals(insertDto.getDescription(), response.getDescription()),
                () -> assertEquals(2, response.getAccidentLossEvents().size()),
                () -> assertEquals(firstEventId, response.getAccidentLossEvents().getFirst().getId()),
                () -> assertEquals(secondEventId, response.getAccidentLossEvents().get(1).getId()),
                () -> verify(castHazardRepository, times(1)).save(any(CastHazard.class))
        );
    }

    @Test
    @DisplayName("#insert > When event ids are null > Create CAST hazard with empty event list")
    void insertWhenEventIdsAreNullCreateCastHazardWithEmptyEventList() {
        CastHazardInsertDto insertDto = assembleCastHazardInsertDto.get();
        insertDto.setAccidentLossEventIds(null);

        when(microStampClient.getAnalysisById(insertDto.getAnalysisId())).thenReturn(null);
        when(castHazardRepository.save(any(CastHazard.class))).thenAnswer(answer -> answer.getArguments()[0]);

        CastHazardReadDto response = service.insert(insertDto);

        assertAll(
                () -> assertEquals(insertDto.getCode(), response.getCode()),
                () -> assertTrue(response.getAccidentLossEvents().isEmpty()),
                () -> verify(accidentLossEventRepository, never()).findById(any(UUID.class)),
                () -> verify(castHazardRepository, times(1)).save(any(CastHazard.class))
        );
    }

    @Test
    @DisplayName("#update > When input is invalid > Throw an exception")
    void updateWhenInputIsInvalidThrowAnException() {
        assertThrows(Step1IllegalArgumentException.class, () -> service.update(UUID.randomUUID(), null));
    }

    @Test
    @DisplayName("#update > When CAST hazard does not exist > Throw an exception")
    void updateWhenCastHazardDoesNotExistThrowAnException() {
        UUID mockId = UUID.randomUUID();

        when(castHazardRepository.findById(mockId)).thenReturn(Optional.empty());

        assertThrows(Step1NotFoundException.class, () -> service.update(mockId, assembleCastHazardUpdateDto.get()));
    }

    @Test
    @DisplayName("#update > When associated accident/loss event is not found > Throw an exception")
    void updateWhenAssociatedAccidentLossEventIsNotFoundThrowAnException() {
        UUID mockId = UUID.randomUUID();
        CastHazard mock = assembleCastHazard.apply(1);
        CastHazardUpdateDto updateDto = assembleCastHazardUpdateDto.get();

        when(castHazardRepository.findById(mockId)).thenReturn(Optional.of(mock));
        when(accidentLossEventRepository.findById(updateDto.getAccidentLossEventIds().getFirst())).thenReturn(Optional.empty());

        assertThrows(Step1NotFoundException.class, () -> service.update(mockId, updateDto));
    }

    @Test
    @DisplayName("#update > When input is valid > Update CAST hazard")
    void updateWhenInputIsValidUpdateCastHazard() {
        UUID mockId = UUID.randomUUID();
        CastHazard mock = assembleCastHazard.apply(1);
        CastHazardUpdateDto updateDto = assembleCastHazardUpdateDto.get();

        UUID firstEventId = updateDto.getAccidentLossEventIds().getFirst();
        UUID secondEventId = updateDto.getAccidentLossEventIds().get(1);

        when(castHazardRepository.findById(mockId)).thenReturn(Optional.of(mock));
        when(accidentLossEventRepository.findById(firstEventId)).thenReturn(Optional.of(assembleAccidentLossEvent.apply(firstEventId, 1)));
        when(accidentLossEventRepository.findById(secondEventId)).thenReturn(Optional.of(assembleAccidentLossEvent.apply(secondEventId, 2)));

        service.update(mockId, updateDto);

        assertAll(
                () -> assertEquals(updateDto.getCode(), mock.getCode()),
                () -> assertEquals(updateDto.getName(), mock.getName()),
                () -> assertEquals(updateDto.getDescription(), mock.getDescription()),
                () -> assertEquals(2, mock.getAccidentLossEvents().size()),
                () -> verify(castHazardRepository, times(1)).save(mock)
        );
    }

    @Test
    @DisplayName("#delete > When CAST hazard does not exist > Throw an exception")
    void deleteWhenCastHazardDoesNotExistThrowAnException() {
        UUID mockId = UUID.randomUUID();

        when(castHazardRepository.findById(mockId)).thenReturn(Optional.empty());

        assertThrows(Step1NotFoundException.class, () -> service.delete(mockId));
    }

    @Test
    @DisplayName("#delete > When CAST hazard exists > Delete it")
    void deleteWhenCastHazardExistsDeleteIt() {
        UUID mockId = UUID.randomUUID();
        CastHazard mock = assembleCastHazard.apply(1);
        mock.setId(mockId);

        when(castHazardRepository.findById(mockId)).thenReturn(Optional.of(mock));

        service.delete(mockId);

        assertAll(
                () -> verify(castHazardRepository, times(1)).findById(mockId),
                () -> verify(castHazardRepository, times(1)).deleteById(mockId)
        );
    }

    private final Function<Integer, CastHazard> assembleCastHazard = index -> CastHazard.builder()
            .id(UUID.randomUUID())
            .code("CH-" + index)
            .name("CAST Hazard " + index)
            .description("Description " + index)
            .accidentLossEvents(List.of())
            .analysisId(UUID.randomUUID())
            .build();

    private final BiFunction<UUID, Integer, AccidentLossEvent> assembleAccidentLossEvent = (uuid, index) -> AccidentLossEvent.builder()
            .id(uuid)
            .code("ALE-" + index)
            .name("Accident/Loss Event " + index)
            .description("Description " + index)
            .analysisId(UUID.randomUUID())
            .build();

    private final Supplier<CastHazardInsertDto> assembleCastHazardInsertDto = () -> CastHazardInsertDto.builder()
            .code("CH-1")
            .name("CAST Hazard")
            .description("Description")
            .accidentLossEventIds(List.of(UUID.randomUUID(), UUID.randomUUID()))
            .analysisId(UUID.randomUUID())
            .build();

    private final Supplier<CastHazardUpdateDto> assembleCastHazardUpdateDto = () -> CastHazardUpdateDto.builder()
            .code("CH-U")
            .name("Updated CAST Hazard")
            .description("Updated description")
            .accidentLossEventIds(List.of(UUID.randomUUID(), UUID.randomUUID()))
            .build();
}