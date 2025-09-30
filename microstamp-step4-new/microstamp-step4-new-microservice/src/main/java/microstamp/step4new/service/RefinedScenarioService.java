package microstamp.step4new.service;

import microstamp.step4new.dto.refinedscenario.RefinedScenarioCommonCauseGroupReadDto;
import microstamp.step4new.dto.refinedscenario.RefinedScenarioInsertDto;
import microstamp.step4new.dto.refinedscenario.RefinedScenarioReadDto;
import microstamp.step4new.dto.refinedscenario.RefinedScenarioUpdateDto;

import java.util.List;
import java.util.UUID;

public interface RefinedScenarioService {

	List<RefinedScenarioReadDto> findByUnsafeControlActionId(UUID unsafeControlActionId);

	List<RefinedScenarioCommonCauseGroupReadDto> groupByCommonCauseForUnsafeControlAction(UUID unsafeControlActionId);

	RefinedScenarioReadDto insert(RefinedScenarioInsertDto dto);

	void update(UUID id, RefinedScenarioUpdateDto dto);

	void delete(UUID id);
}