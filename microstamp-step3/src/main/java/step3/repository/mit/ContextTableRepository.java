package step3.repository.mit;

import org.springframework.data.jpa.repository.JpaRepository;
import step3.entity.mit.ContextTable;

import java.util.Optional;
import java.util.UUID;

public interface ContextTableRepository extends JpaRepository<ContextTable, UUID> {
    Optional<ContextTable> findByConnectionId(UUID connectionId);
}
