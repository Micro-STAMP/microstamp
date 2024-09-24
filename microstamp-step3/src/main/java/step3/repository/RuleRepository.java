package step3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import step3.entity.Rule;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RuleRepository extends JpaRepository<Rule, UUID> {
    List<Rule> findByControlActionId(UUID id);
    List<Rule> findByAnalysisId(UUID id);
    Optional<Rule> findByCode(String code);
    void deleteAllByControlActionId(UUID controlActionId);
}
