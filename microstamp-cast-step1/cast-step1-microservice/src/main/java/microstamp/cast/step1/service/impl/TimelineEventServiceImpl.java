package microstamp.cast.step1.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
//import microstamp.cast.step1.client.MicroStampClient;
import microstamp.cast.step1.dto.timelineevent.TimelineEventInsertDto;
import microstamp.cast.step1.dto.timelineevent.TimelineEventReadDto;
import microstamp.cast.step1.dto.timelineevent.TimelineEventUpdateDto;
import microstamp.cast.step1.entity.TimelineEvent;
import microstamp.cast.step1.exception.Step1IllegalArgumentException;
import microstamp.cast.step1.exception.Step1NotFoundException;
import microstamp.cast.step1.mapper.TimelineEventMapper;
import microstamp.cast.step1.repository.TimelineEventRepository;
import microstamp.cast.step1.service.TimelineEventService;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Log4j2
@Component
@AllArgsConstructor
public class TimelineEventServiceImpl implements TimelineEventService {

    private final TimelineEventRepository timelineEventRepository;

    //private final MicroStampClient microStampClient;

    public List<TimelineEventReadDto> findAll() {
        log.info("Finding all CAST timeline events");
        return timelineEventRepository.findAll().stream()
                .map(TimelineEventMapper::toDto)
                .sorted(getTimelineEventComparator())
                .toList();
    }

    public TimelineEventReadDto findById(UUID id) throws Step1NotFoundException {
        log.info("Finding CAST timeline event by id: {}", id);
        return TimelineEventMapper.toDto(timelineEventRepository.findById(id)
                .orElseThrow(() -> new Step1NotFoundException("TimelineEvent", id.toString())));
    }

    public List<TimelineEventReadDto> findByAnalysisId(UUID id) {
        log.info("Finding CAST timeline events by analysis id: {}", id);
        return timelineEventRepository.findByAnalysisId(id).stream()
                .map(TimelineEventMapper::toDto)
                .sorted(getTimelineEventComparator())
                .toList();
    }

    public TimelineEventReadDto insert(TimelineEventInsertDto timelineEventInsertDto) throws Step1NotFoundException {
        log.debug("Verifying if the CAST timeline event insert is valid");
        if (Objects.isNull(timelineEventInsertDto)) {
            throw new Step1IllegalArgumentException("Unable to create a new timeline event because the provided TimelineEventInsertDto is null.");
        }

        log.info("Verifying if the analysis exists on the database");
        //microStampClient.getAnalysisById(timelineEventInsertDto.getAnalysisId());

        TimelineEvent timelineEvent = TimelineEventMapper.toEntity(timelineEventInsertDto);

        log.info("Inserting the CAST timeline event {} on the database", timelineEvent);
        timelineEventRepository.save(timelineEvent);

        return TimelineEventMapper.toDto(timelineEvent);
    }

    public void update(UUID id, TimelineEventUpdateDto timelineEventUpdateDto) throws Step1NotFoundException {
        log.debug("Verifying if the CAST timeline event update is valid");
        if (Objects.isNull(timelineEventUpdateDto)) {
            throw new Step1IllegalArgumentException("Unable to update the timeline event because the provided TimelineEventUpdateDto is null.");
        }

        log.debug("Finding if there is a CAST timeline event with id {} to update", id);
        TimelineEvent timelineEvent = timelineEventRepository.findById(id)
                .orElseThrow(() -> new Step1NotFoundException("TimelineEvent", id.toString()));

        timelineEvent.setCode(timelineEventUpdateDto.getCode());
        timelineEvent.setEventOrder(timelineEventUpdateDto.getEventOrder());
        timelineEvent.setEventDescription(timelineEventUpdateDto.getEventDescription());
        timelineEvent.setQuestions(timelineEventUpdateDto.getQuestions());

        log.info("Updating the CAST timeline event with id {}", id);
        timelineEventRepository.save(timelineEvent);
    }

    public void delete(UUID id) throws Step1NotFoundException {
        log.debug("Finding if there is a CAST timeline event with id {} to delete", id);
        TimelineEvent timelineEvent = timelineEventRepository.findById(id)
                .orElseThrow(() -> new Step1NotFoundException("TimelineEvent", id.toString()));

        log.info("Deleting the CAST timeline event with id {} on the database", timelineEvent.getId());
        timelineEventRepository.deleteById(timelineEvent.getId());
    }

    private Comparator<TimelineEventReadDto> getTimelineEventComparator() {
        return Comparator
                .comparing(TimelineEventReadDto::getEventOrder, Comparator.nullsLast(Integer::compareTo))
                .thenComparing(TimelineEventReadDto::getCode, Comparator.nullsLast(String::compareTo));
    }
}