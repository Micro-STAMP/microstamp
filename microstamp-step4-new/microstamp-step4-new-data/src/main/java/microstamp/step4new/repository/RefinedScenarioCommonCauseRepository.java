package microstamp.step4new.repository;

import microstamp.step4new.entity.RefinedScenarioCommonCause;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefinedScenarioCommonCauseRepository extends JpaRepository<RefinedScenarioCommonCause, UUID> {
	Optional<RefinedScenarioCommonCause> findByCode(String code);
}