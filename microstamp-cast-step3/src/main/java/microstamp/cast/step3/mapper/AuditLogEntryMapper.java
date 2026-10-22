package microstamp.cast.step3.mapper;

import microstamp.cast.step3.dto.auditlog.AuditLogEntryReadDto;
import microstamp.cast.step3.entity.AuditLogEntry;

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
