package microstamp.step2.service;

import microstamp.step2.dto.auditlog.AuditLogEntryReadDto;

import java.util.List;
import java.util.UUID;

public interface AuditLogService {

    List<AuditLogEntryReadDto> findRecentByAnalysisId(UUID analysisId, int limit);

    void record(UUID analysisId, String step, String fieldName);
}
