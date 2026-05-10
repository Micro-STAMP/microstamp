package microstamp.cast.step1.service;

import microstamp.cast.step1.dto.timelineevent.TimelineEventInsertDto;
import microstamp.cast.step1.dto.timelineevent.TimelineEventReadDto;
import microstamp.cast.step1.dto.timelineevent.TimelineEventUpdateDto;

import java.util.List;
import java.util.UUID;

public interface TimelineEventService {

    List<TimelineEventReadDto> findAll();

    TimelineEventReadDto findById(UUID id);

    List<TimelineEventReadDto> findByAnalysisId(UUID id);

    TimelineEventReadDto insert(TimelineEventInsertDto timelineEventInsertDto);

    void update(UUID id, TimelineEventUpdateDto timelineEventUpdateDto);

    void delete(UUID id);
}