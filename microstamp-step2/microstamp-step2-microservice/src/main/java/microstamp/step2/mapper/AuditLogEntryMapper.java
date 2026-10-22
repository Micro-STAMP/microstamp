package microstamp.step2.mapper;

import microstamp.step2.dto.auditlog.AuditLogEntryReadDto;
import microstamp.step2.entity.AuditLogEntry;

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
