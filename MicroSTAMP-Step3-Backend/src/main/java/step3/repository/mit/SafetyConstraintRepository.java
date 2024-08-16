package step3.repository.mit;

import org.springframework.data.jpa.repository.JpaRepository;
import step3.entity.mit.SafetyConstraint;

import java.util.UUID;

public interface SafetyConstraintRepository extends JpaRepository<SafetyConstraint, UUID> {
    SafetyConstraint findByUnsafeControlActionId(UUID ucaId);
}
