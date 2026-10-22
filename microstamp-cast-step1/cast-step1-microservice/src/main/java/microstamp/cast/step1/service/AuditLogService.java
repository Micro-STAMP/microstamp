package microstamp.cast.step1.service;

import microstamp.cast.step1.dto.auditlog.AuditLogEntryReadDto;

import java.util.List;
import java.util.UUID;

public interface AuditLogService {

    List<AuditLogEntryReadDto> findRecentByAnalysisId(UUID analysisId, int limit);

    void record(UUID analysisId, String step, String fieldName);
}
