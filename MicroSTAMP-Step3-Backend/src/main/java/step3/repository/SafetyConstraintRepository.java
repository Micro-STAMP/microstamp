package step3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import step3.entity.ContextTable;
import step3.entity.SafetyConstraint;

public interface SafetyConstraintRepository extends JpaRepository<SafetyConstraint, Long> {
    SafetyConstraint findByUnsafeControlActionId(Long ucaId);
}
