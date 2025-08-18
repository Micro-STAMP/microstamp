package microstamp.step4new.repository;

import microstamp.step4new.entity.RefinedScenario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RefinedScenarioRepository extends JpaRepository<RefinedScenario, UUID> {
	List<RefinedScenario> findByUnsafeControlActionId(UUID unsafeControlActionId);
}