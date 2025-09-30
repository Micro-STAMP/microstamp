package microstamp.step4new.service;

import microstamp.step4new.dto.refinedscenario.RefinedScenarioTemplateReadDto;
import microstamp.step4new.dto.refinedscenario.RefinedScenarioCommonCauseReadDto;

import java.util.List;
import java.util.UUID;

public interface RefinedScenarioTemplateService {

	List<RefinedScenarioTemplateReadDto> findByCommonCauseCode(String code);

	List<RefinedScenarioTemplateReadDto> findAll();

	List<RefinedScenarioTemplateReadDto> applyTemplates(UUID unsafeControlActionId);

	List<RefinedScenarioTemplateReadDto> applyTemplatesByCommonCause(UUID unsafeControlActionId, String commonCauseCode);

	List<RefinedScenarioCommonCauseReadDto> applyTemplatesGroupedByCommonCause(UUID unsafeControlActionId);
}