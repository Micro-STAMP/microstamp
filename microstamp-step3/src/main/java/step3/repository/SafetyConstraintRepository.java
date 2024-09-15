package step3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import step3.entity.SafetyConstraint;

import java.util.Optional;
import java.util.UUID;

public interface SafetyConstraintRepository extends JpaRepository<SafetyConstraint, UUID> {
    Optional<SafetyConstraint> findByUnsafeControlActionId(UUID ucaId);
}
