package microstamp.cast.step4.mapper;

import microstamp.cast.step4.dto.auditlog.AuditLogEntryReadDto;
import microstamp.cast.step4.entity.AuditLogEntry;

public class AuditLogEntryMapper {

    public static AuditLogEntryReadDto toDto(AuditLogEntry entity) {
        return AuditLogEntryReadDto.builder()
                .id(entity.getId())
                .step(entity.getStep())
                .fieldName(entity.getFieldName())
                .timestamp(entity.getTimestamp())
                .build();
    }
}
