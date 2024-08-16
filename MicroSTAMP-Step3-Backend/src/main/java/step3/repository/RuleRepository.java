package step3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import step3.entity.Rule;
import java.util.List;

public interface RuleRepository extends JpaRepository<Rule, Long> {
    List<Rule> findByControlActionId(Long id);
}
