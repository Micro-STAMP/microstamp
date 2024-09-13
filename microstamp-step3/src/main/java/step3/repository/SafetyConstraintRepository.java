package step3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import step3.entity.SafetyConstraint;

import java.util.UUID;

public interface SafetyConstraintRepository extends JpaRepository<SafetyConstraint, UUID> {
    SafetyConstraint findByUnsafeControlActionId(UUID ucaId);
}
