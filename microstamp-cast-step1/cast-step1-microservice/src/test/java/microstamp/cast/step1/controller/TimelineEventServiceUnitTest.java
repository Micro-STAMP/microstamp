package microstamp.cast.step1.unit;

import microstamp.cast.step1.client.MicroStampClient;
import microstamp.cast.step1.dto.timelineevent.TimelineEventInsertDto;
import microstamp.cast.step1.dto.timelineevent.TimelineEventReadDto;
import microstamp.cast.step1.dto.timelineevent.TimelineEventUpdateDto;
import microstamp.cast.step1.entity.TimelineEvent;
import microstamp.cast.step1.exception.Step1IllegalArgumentException;
import microstamp.cast.step1.exception.Step1NotFoundException;
import microstamp.cast.step1.repository.TimelineEventRepository;
import microstamp.cast.step1.service.impl.TimelineEventServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TimelineEventServiceUnitTest {

    @InjectMocks
    private TimelineEventServiceImpl service;

    @Mock
    private TimelineEventRepository timelineEventRepository;

    @Mock
    private MicroStampClient microStampClient;

    @Test
    @DisplayName("#findAll > When no timeline event is found > Return an empty list")
    void findAllWhenNoTimelineEventIsFoundReturnAnEmptyList() {
        when(timelineEventRepository.findAll()).thenReturn(List.of());

        List<TimelineEventReadDto> response = service.findAll();

        assertTrue(response.isEmpty());
    }

    @Test
    @DisplayName("#findAll > When there are timeline events found > Return them sorted by event order")
    void findAllWhenThereAreTimelineEventsFoundReturnThemSortedByEventOrder() {
        when(timelineEventRepository.findAll()).thenReturn(List.of(
                assembleTimelineEvent.apply(2),
                assembleTimelineEvent.apply(1)
        ));

        List<TimelineEventReadDto> response = service.findAll();

        assertAll(
                () -> Assertions.assertFalse(response.isEmpty()),
                () -> assertEquals(2, response.size()),
                () -> assertEquals(1, response.getFirst().getEventOrder()),
                () -> assertEquals("TE-1", response.getFirst().getCode()),
                () -> assertEquals(2, response.get(1).getEventOrder()),
                () -> assertEquals("TE-2", response.get(1).getCode())
        );
    }

    @Test
    @DisplayName("#findById > When no timeline event is found > Throw an exception")
    void findByIdWhenNoTimelineEventIsFoundThrowAnException() {
        UUID mockId = UUID.randomUUID();

        when(timelineEventRepository.findById(mockId)).thenReturn(Optional.empty());

        assertThrows(Step1NotFoundException.class, () -> service.findById(mockId));
    }

    @Test
    @DisplayName("#findById > When timeline event is found > Return it")
    void findByIdWhenTimelineEventIsFoundReturnIt() {
        UUID mockId = UUID.randomUUID();
        TimelineEvent mock = assembleTimelineEvent.apply(1);
        mock.setId(mockId);

        when(timelineEventRepository.findById(mockId)).thenReturn(Optional.of(mock));

        TimelineEventReadDto response = service.findById(mockId);

        assertAll(
                () -> assertEquals(mock.getId(), response.getId()),
                () -> assertEquals(mock.getCode(), response.getCode()),
                () -> assertEquals(mock.getEventOrder(), response.getEventOrder()),
                () -> assertEquals(mock.getEventDescription(), response.getEventDescription()),
                () -> assertEquals(mock.getQuestions(), response.getQuestions())
        );
    }

    @Test
    @DisplayName("#findByAnalysisId > When no timeline event is found > Return an empty list")
    void findByAnalysisIdWhenNoTimelineEventIsFoundReturnAnEmptyList() {
        UUID mockAnalysisId = UUID.randomUUID();

        when(timelineEventRepository.findByAnalysisId(mockAnalysisId)).thenReturn(List.of());

        List<TimelineEventReadDto> response = service.findByAnalysisId(mockAnalysisId);

        assertTrue(response.isEmpty());
    }

    @Test
    @DisplayName("#findByAnalysisId > When there are timeline events found > Return them sorted by event order")
    void findByAnalysisIdWhenThereAreTimelineEventsFoundReturnThemSortedByEventOrder() {
        UUID mockAnalysisId = UUID.randomUUID();

        when(timelineEventRepository.findByAnalysisId(mockAnalysisId)).thenReturn(List.of(
                assembleTimelineEvent.apply(2),
                assembleTimelineEvent.apply(1)
        ));

        List<TimelineEventReadDto> response = service.findByAnalysisId(mockAnalysisId);

        assertAll(
                () -> Assertions.assertFalse(response.isEmpty()),
                () -> assertEquals(2, response.size()),
                () -> assertEquals(1, response.getFirst().getEventOrder()),
                () -> assertEquals(2, response.get(1).getEventOrder())
        );
    }

    @Test
    @DisplayName("#insert > When input is invalid > Throw an exception")
    void insertWhenInputIsInvalidThrowAnException() {
        assertThrows(Step1IllegalArgumentException.class, () -> service.insert(null));
    }

    @Test
    @DisplayName("#insert > When input is valid > Create timeline event")
    void insertWhenInputIsValidCreateTimelineEvent() {
        TimelineEventInsertDto insertDto = assembleTimelineEventInsertDto.get();

        when(microStampClient.getAnalysisById(insertDto.getAnalysisId())).thenReturn(null);
        when(timelineEventRepository.save(any(TimelineEvent.class))).thenAnswer(answer -> answer.getArguments()[0]);

        TimelineEventReadDto response = service.insert(insertDto);

        assertAll(
                () -> assertEquals(insertDto.getCode(), response.getCode()),
                () -> assertEquals(insertDto.getEventOrder(), response.getEventOrder()),
                () -> assertEquals(insertDto.getEventDescription(), response.getEventDescription()),
                () -> assertEquals(insertDto.getQuestions(), response.getQuestions()),
                () -> verify(microStampClient, times(1)).getAnalysisById(insertDto.getAnalysisId()),
                () -> verify(timelineEventRepository, times(1)).save(any(TimelineEvent.class))
        );
    }

    @Test
    @DisplayName("#update > When input is invalid > Throw an exception")
    void updateWhenInputIsInvalidThrowAnException() {
        assertThrows(Step1IllegalArgumentException.class, () -> service.update(UUID.randomUUID(), null));
    }

    @Test
    @DisplayName("#update > When timeline event does not exist > Throw an exception")
    void updateWhenTimelineEventDoesNotExistThrowAnException() {
        UUID mockId = UUID.randomUUID();

        when(timelineEventRepository.findById(mockId)).thenReturn(Optional.empty());

        assertThrows(Step1NotFoundException.class, () -> service.update(mockId, assembleTimelineEventUpdateDto.get()));
    }

    @Test
    @DisplayName("#update > When timeline event exists > Update it")
    void updateWhenTimelineEventExistsUpdateIt() {
        UUID mockId = UUID.randomUUID();
        TimelineEvent mock = assembleTimelineEvent.apply(1);
        TimelineEventUpdateDto updateDto = assembleTimelineEventUpdateDto.get();

        when(timelineEventRepository.findById(mockId)).thenReturn(Optional.of(mock));

        service.update(mockId, updateDto);

        assertAll(
                () -> assertEquals(updateDto.getCode(), mock.getCode()),
                () -> assertEquals(updateDto.getEventOrder(), mock.getEventOrder()),
                () -> assertEquals(updateDto.getEventDescription(), mock.getEventDescription()),
                () -> assertEquals(updateDto.getQuestions(), mock.getQuestions()),
                () -> verify(timelineEventRepository, times(1)).save(mock)
        );
    }

    @Test
    @DisplayName("#delete > When timeline event does not exist > Throw an exception")
    void deleteWhenTimelineEventDoesNotExistThrowAnException() {
        UUID mockId = UUID.randomUUID();

        when(timelineEventRepository.findById(mockId)).thenReturn(Optional.empty());

        assertThrows(Step1NotFoundException.class, () -> service.delete(mockId));
    }

    @Test
    @DisplayName("#delete > When timeline event exists > Delete it")
    void deleteWhenTimelineEventExistsDeleteIt() {
        UUID mockId = UUID.randomUUID();
        TimelineEvent mock = assembleTimelineEvent.apply(1);
        mock.setId(mockId);

        when(timelineEventRepository.findById(mockId)).thenReturn(Optional.of(mock));

        service.delete(mockId);

        assertAll(
                () -> verify(timelineEventRepository, times(1)).findById(mockId),
                () -> verify(timelineEventRepository, times(1)).deleteById(mockId)
        );
    }

    private final Function<Integer, TimelineEvent> assembleTimelineEvent = index -> TimelineEvent.builder()
            .id(UUID.randomUUID())
            .code("TE-" + index)
            .eventOrder(index)
            .eventDescription("Timeline event " + index)
            .questions("Question " + index)
            .analysisId(UUID.randomUUID())
            .build();

    private final Supplier<TimelineEventInsertDto> assembleTimelineEventInsertDto = () -> TimelineEventInsertDto.builder()
            .code("TE-1")
            .eventOrder(1)
            .eventDescription("Timeline event")
            .questions("Questions")
            .analysisId(UUID.randomUUID())
            .build();

    private final Supplier<TimelineEventUpdateDto> assembleTimelineEventUpdateDto = () -> TimelineEventUpdateDto.builder()
            .code("TE-U")
            .eventOrder(10)
            .eventDescription("Updated timeline event")
            .questions("Updated questions")
            .build();
}