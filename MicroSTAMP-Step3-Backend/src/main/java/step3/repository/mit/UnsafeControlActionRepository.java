package step3.repository.mit;

import org.springframework.data.jpa.repository.JpaRepository;
import step3.entity.mit.UnsafeControlAction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UnsafeControlActionRepository extends JpaRepository<UnsafeControlAction, UUID> {
    List<UnsafeControlAction> findByControlActionId(UUID id);
    List<UnsafeControlAction> findByRuleTag(String tag);
    List<UnsafeControlAction> findByControlActionControllerId(UUID id);
    Optional<UnsafeControlAction> findFirstByName(String name);
}
