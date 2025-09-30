package microstamp.step4new.repository;

import microstamp.step4new.book.RefinedScenarioBook;
import microstamp.step4new.entity.RefinedScenario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RefinedScenarioRepository extends JpaRepository<RefinedScenario, UUID> {
	List<RefinedScenario> findByUnsafeControlActionId(UUID unsafeControlActionId);
	
	@Query(RefinedScenarioBook.FIND_MAX_CODE_NUMBER_BY_ANALYSIS_ID)
	Integer findMaxCodeNumberByAnalysisId(@Param("analysisId") UUID analysisId);
}