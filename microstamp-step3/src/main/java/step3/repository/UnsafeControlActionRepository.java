package step3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import step3.entity.UnsafeControlAction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UnsafeControlActionRepository extends JpaRepository<UnsafeControlAction, UUID> {
    List<UnsafeControlAction> findByControlActionId(UUID id);
    List<UnsafeControlAction> findByRuleCode(String code);
    List<UnsafeControlAction> findByAnalysisId(UUID id);
}
