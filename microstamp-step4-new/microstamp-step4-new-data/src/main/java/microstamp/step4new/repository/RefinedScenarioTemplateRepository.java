package microstamp.step4new.repository;

import microstamp.step3.dto.UCAType;
import microstamp.step4new.entity.RefinedScenarioTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RefinedScenarioTemplateRepository extends JpaRepository<RefinedScenarioTemplate, UUID> {
	List<RefinedScenarioTemplate> findByCommonCause_Id(UUID commonCauseId);
	List<RefinedScenarioTemplate> findByCommonCause_Code(String code);
	List<RefinedScenarioTemplate> findByUnsafeControlActionType(UCAType type);
	List<RefinedScenarioTemplate> findByUnsafeControlActionTypeIsNull();
}