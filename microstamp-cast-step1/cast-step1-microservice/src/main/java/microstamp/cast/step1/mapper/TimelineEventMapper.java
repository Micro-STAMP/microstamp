package microstamp.cast.step1.mapper;

import microstamp.cast.step1.dto.timelineevent.TimelineEventInsertDto;
import microstamp.cast.step1.dto.timelineevent.TimelineEventReadDto;
import microstamp.cast.step1.entity.TimelineEvent;

public class TimelineEventMapper {

    public static TimelineEventReadDto toDto(TimelineEvent entity) {
        return TimelineEventReadDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .eventOrder(entity.getEventOrder())
                .eventDescription(entity.getEventDescription())
                .questions(entity.getQuestions())
                .timeLabel(entity.getTimeLabel())
                .exactDateTime(entity.getExactDateTime())
                .imageBase64(entity.getImageBase64())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static TimelineEvent toEntity(TimelineEventInsertDto dto) {
        return TimelineEvent.builder()
                .code(dto.getCode())
                .eventOrder(dto.getEventOrder())
                .eventDescription(dto.getEventDescription())
                .questions(dto.getQuestions())
                .timeLabel(dto.getTimeLabel())
                .exactDateTime(dto.getExactDateTime())
                .analysisId(dto.getAnalysisId())
                .build();
    }
}