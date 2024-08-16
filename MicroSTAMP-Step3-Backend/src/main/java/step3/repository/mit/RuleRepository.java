package step3.repository.mit;

import org.springframework.data.jpa.repository.JpaRepository;
import step3.entity.mit.Rule;

import java.util.List;
import java.util.UUID;

public interface RuleRepository extends JpaRepository<Rule, UUID> {
    List<Rule> findByControlActionId(UUID id);
}
