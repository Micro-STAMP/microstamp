package microstamp.step4new.repository;

import microstamp.step4new.entity.HighLevelSolution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HighLevelSolutionRepository extends JpaRepository<HighLevelSolution, UUID> {

    Optional<HighLevelSolution> findByFormalScenarioClassId(UUID formalScenarioClassId);

	List<HighLevelSolution> findByFormalScenarioClass_FormalScenario_UnsafeControlActionId(UUID unsafeControlActionId);

}