package microstamp.step4new.repository;

import microstamp.step4new.entity.Mitigation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MitigationRepository extends JpaRepository<Mitigation, UUID> {

	Optional<Mitigation> findByRefinedScenarioId(UUID refinedScenarioId);

	List<Mitigation> findByRefinedScenario_UnsafeControlActionId(UUID unsafeControlActionId);
}


