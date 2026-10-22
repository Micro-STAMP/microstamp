package microstamp.step2.service.impl;

import lombok.AllArgsConstructor;
import microstamp.step2.entity.AuditLogEntry;
import microstamp.step2.mapper.AuditLogEntryMapper;
import microstamp.step2.dto.auditlog.AuditLogEntryReadDto;
import microstamp.step2.repository.AuditLogEntryRepository;
import microstamp.step2.service.AuditLogService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogEntryRepository auditLogEntryRepository;

    @Override
    public List<AuditLogEntryReadDto> findRecentByAnalysisId(UUID analysisId, int limit) {
        return auditLogEntryRepository.findByAnalysisIdOrderByTimestampDesc(analysisId, PageRequest.of(0, limit)).stream()
                .map(AuditLogEntryMapper::toDto)
                .toList();
    }

    @Override
    public void record(UUID analysisId, String step, String fieldName) {
        AuditLogEntry entry = AuditLogEntry.builder()
                .analysisId(analysisId)
                .step(step)
                .fieldName(fieldName)
                .timestamp(Instant.now())
                .build();
        auditLogEntryRepository.save(entry);
    }
}
