package microstamp.cast.step3.service;

import microstamp.cast.step3.dto.auditlog.AuditLogEntryReadDto;

import java.util.List;
import java.util.UUID;

public interface AuditLogService {

    List<AuditLogEntryReadDto> findRecentByAnalysisId(UUID analysisId, int limit);

    void record(UUID analysisId, String step, String fieldName);
}
