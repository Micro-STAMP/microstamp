package microstamp.cast.step5.repository;

import microstamp.cast.step5.entity.AuditLogEntry;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuditLogEntryRepository extends JpaRepository<AuditLogEntry, UUID> {

    List<AuditLogEntry> findByAnalysisIdOrderByTimestampDesc(UUID analysisId, Pageable pageable);

}
