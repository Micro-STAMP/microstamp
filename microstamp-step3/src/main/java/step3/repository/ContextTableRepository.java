package step3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import step3.entity.ContextTable;

import java.util.Optional;
import java.util.UUID;

public interface ContextTableRepository extends JpaRepository<ContextTable, UUID> {
    Optional<ContextTable> findByControlActionId(UUID controlActionId);
}
