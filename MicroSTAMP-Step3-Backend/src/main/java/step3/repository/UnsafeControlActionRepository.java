package step3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import step3.entity.UnsafeControlAction;

import java.util.List;
import java.util.Optional;

public interface UnsafeControlActionRepository extends JpaRepository<UnsafeControlAction, Long> {
    List<UnsafeControlAction> findByControlActionId(Long id);
    List<UnsafeControlAction> findByRuleTag(String tag);
    List<UnsafeControlAction> findByControlActionControllerId(Long id);
    Optional<UnsafeControlAction> findFirstByName(String name);
}
