package microstamp.cast.step1.unit;

import microstamp.cast.step1.client.MicroStampClient;
import microstamp.cast.step1.dto.accidentlossevent.AccidentLossEventInsertDto;
import microstamp.cast.step1.dto.accidentlossevent.AccidentLossEventReadDto;
import microstamp.cast.step1.dto.accidentlossevent.AccidentLossEventUpdateDto;
import microstamp.cast.step1.entity.AccidentLossEvent;
import microstamp.cast.step1.exception.Step1IllegalArgumentException;
import microstamp.cast.step1.exception.Step1NotFoundException;
import microstamp.cast.step1.repository.AccidentLossEventRepository;
import microstamp.cast.step1.service.impl.AccidentLossEventServiceImpl;
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
public class AccidentLossEventServiceUnitTest {

    @InjectMocks
    private AccidentLossEventServiceImpl service;

    @Mock
    private AccidentLossEventRepository accidentLossEventRepository;

    @Mock
    private MicroStampClient microStampClient;

    @Test
    @DisplayName("#findAll > When no accident/loss event is found > Return an empty list")
    void findAllWhenNoAccidentLossEventIsFoundReturnAnEmptyList() {
        when(accidentLossEventRepository.findAll()).thenReturn(List.of());

        List<AccidentLossEventReadDto> response = service.findAll();

        assertTrue(response.isEmpty());
    }

    @Test
    @DisplayName("#findAll > When there are accident/loss events found > Return them sorted by code")
    void findAllWhenThereAreAccidentLossEventsFoundReturnThemSortedByCode() {
        when(accidentLossEventRepository.findAll()).thenReturn(List.of(
                assembleAccidentLossEvent.apply(2),
                assembleAccidentLossEvent.apply(1)
        ));

        List<AccidentLossEventReadDto> response = service.findAll();

        assertAll(
                () -> assertEquals(2, response.size()),
                () -> assertEquals("ALE-1", response.getFirst().getCode()),
                () -> assertEquals("Accident/Loss Event 1", response.getFirst().getName()),
                () -> assertEquals("ALE-2", response.get(1).getCode()),
                () -> assertEquals("Accident/Loss Event 2", response.get(1).getName())
        );
    }

    @Test
    @DisplayName("#findById > When no accident/loss event is found > Throw an exception")
    void findByIdWhenNoAccidentLossEventIsFoundThrowAnException() {
        UUID mockId = UUID.randomUUID();

        when(accidentLossEventRepository.findById(mockId)).thenReturn(Optional.empty());

        assertThrows(Step1NotFoundException.class, () -> service.findById(mockId));
    }

    @Test
    @DisplayName("#findById > When accident/loss event is found > Return it")
    void findByIdWhenAccidentLossEventIsFoundReturnIt() {
        UUID mockId = UUID.randomUUID();
        AccidentLossEvent mock = assembleAccidentLossEvent.apply(1);
        mock.setId(mockId);

        when(accidentLossEventRepository.findById(mockId)).thenReturn(Optional.of(mock));

        AccidentLossEventReadDto response = service.findById(mockId);

        assertAll(
                () -> assertEquals(mock.getId(), response.getId()),
                () -> assertEquals(mock.getCode(), response.getCode()),
                () -> assertEquals(mock.getName(), response.getName()),
                () -> assertEquals(mock.getDescription(), response.getDescription())
        );
    }

    @Test
    @DisplayName("#findByAnalysisId > When no accident/loss event is found > Return an empty list")
    void findByAnalysisIdWhenNoAccidentLossEventIsFoundReturnAnEmptyList() {
        UUID mockAnalysisId = UUID.randomUUID();

        when(accidentLossEventRepository.findByAnalysisId(mockAnalysisId)).thenReturn(List.of());

        List<AccidentLossEventReadDto> response = service.findByAnalysisId(mockAnalysisId);

        assertTrue(response.isEmpty());
    }

    @Test
    @DisplayName("#findByAnalysisId > When there are accident/loss events found > Return them")
    void findByAnalysisIdWhenThereAreAccidentLossEventsFoundReturnThem() {
        UUID mockAnalysisId = UUID.randomUUID();

        when(accidentLossEventRepository.findByAnalysisId(mockAnalysisId)).thenReturn(List.of(
                assembleAccidentLossEvent.apply(1),
                assembleAccidentLossEvent.apply(2)
        ));

        List<AccidentLossEventReadDto> response = service.findByAnalysisId(mockAnalysisId);

        assertAll(
                () -> Assertions.assertFalse(response.isEmpty()),
                () -> assertEquals(2, response.size()),
                () -> assertEquals("ALE-1", response.getFirst().getCode()),
                () -> assertEquals("ALE-2", response.get(1).getCode())
        );
    }

    @Test
    @DisplayName("#insert > When input is invalid > Throw an exception")
    void insertWhenInputIsInvalidThrowAnException() {
        assertThrows(Step1IllegalArgumentException.class, () -> service.insert(null));
    }

    @Test
    @DisplayName("#insert > When input is valid > Create accident/loss event")
    void insertWhenInputIsValidCreateAccidentLossEvent() {
        AccidentLossEventInsertDto insertDto = assembleAccidentLossEventInsertDto.get();

        when(microStampClient.getAnalysisById(insertDto.getAnalysisId())).thenReturn(null);
        when(accidentLossEventRepository.save(any(AccidentLossEvent.class))).thenAnswer(answer -> answer.getArguments()[0]);

        AccidentLossEventReadDto response = service.insert(insertDto);

        assertAll(
                () -> assertEquals(insertDto.getCode(), response.getCode()),
                () -> assertEquals(insertDto.getName(), response.getName()),
                () -> assertEquals(insertDto.getDescription(), response.getDescription()),
                () -> verify(microStampClient, times(1)).getAnalysisById(insertDto.getAnalysisId()),
                () -> verify(accidentLossEventRepository, times(1)).save(any(AccidentLossEvent.class))
        );
    }

    @Test
    @DisplayName("#update > When input is invalid > Throw an exception")
    void updateWhenInputIsInvalidThrowAnException() {
        assertThrows(Step1IllegalArgumentException.class, () -> service.update(UUID.randomUUID(), null));
    }

    @Test
    @DisplayName("#update > When accident/loss event does not exist > Throw an exception")
    void updateWhenAccidentLossEventDoesNotExistThrowAnException() {
        UUID mockId = UUID.randomUUID();

        when(accidentLossEventRepository.findById(mockId)).thenReturn(Optional.empty());

        assertThrows(Step1NotFoundException.class, () -> service.update(mockId, assembleAccidentLossEventUpdateDto.get()));
    }

    @Test
    @DisplayName("#update > When accident/loss event exists > Update it")
    void updateWhenAccidentLossEventExistsUpdateIt() {
        UUID mockId = UUID.randomUUID();
        AccidentLossEvent mock = assembleAccidentLossEvent.apply(1);
        AccidentLossEventUpdateDto updateDto = assembleAccidentLossEventUpdateDto.get();

        when(accidentLossEventRepository.findById(mockId)).thenReturn(Optional.of(mock));

        service.update(mockId, updateDto);

        assertAll(
                () -> assertEquals(updateDto.getCode(), mock.getCode()),
                () -> assertEquals(updateDto.getName(), mock.getName()),
                () -> assertEquals(updateDto.getDescription(), mock.getDescription()),
                () -> verify(accidentLossEventRepository, times(1)).save(mock)
        );
    }

    @Test
    @DisplayName("#delete > When accident/loss event does not exist > Throw an exception")
    void deleteWhenAccidentLossEventDoesNotExistThrowAnException() {
        UUID mockId = UUID.randomUUID();

        when(accidentLossEventRepository.findById(mockId)).thenReturn(Optional.empty());

        assertThrows(Step1NotFoundException.class, () -> service.delete(mockId));
    }

    @Test
    @DisplayName("#delete > When accident/loss event exists > Delete it")
    void deleteWhenAccidentLossEventExistsDeleteIt() {
        UUID mockId = UUID.randomUUID();
        AccidentLossEvent mock = assembleAccidentLossEvent.apply(1);
        mock.setId(mockId);

        when(accidentLossEventRepository.findById(mockId)).thenReturn(Optional.of(mock));

        service.delete(mockId);

        assertAll(
                () -> verify(accidentLossEventRepository, times(1)).findById(mockId),
                () -> verify(accidentLossEventRepository, times(1)).deleteById(mockId)
        );
    }

    private final Function<Integer, AccidentLossEvent> assembleAccidentLossEvent = index -> AccidentLossEvent.builder()
            .id(UUID.randomUUID())
            .code("ALE-" + index)
            .name("Accident/Loss Event " + index)
            .description("Description " + index)
            .analysisId(UUID.randomUUID())
            .build();

    private final Supplier<AccidentLossEventInsertDto> assembleAccidentLossEventInsertDto = () -> AccidentLossEventInsertDto.builder()
            .code("ALE-1")
            .name("Accident/Loss Event")
            .description("Description")
            .analysisId(UUID.randomUUID())
            .build();

    private final Supplier<AccidentLossEventUpdateDto> assembleAccidentLossEventUpdateDto = () -> AccidentLossEventUpdateDto.builder()
            .code("ALE-U")
            .name("Updated Accident/Loss Event")
            .description("Updated description")
            .build();
}