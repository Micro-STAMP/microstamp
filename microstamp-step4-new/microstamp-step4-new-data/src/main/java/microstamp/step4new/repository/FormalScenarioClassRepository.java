package microstamp.step4new.repository;

import microstamp.step4new.entity.FormalScenarioClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FormalScenarioClassRepository extends JpaRepository<FormalScenarioClass, UUID> {
	List<FormalScenarioClass> findByFormalScenario_UnsafeControlActionId(UUID unsafeControlActionId);
}
