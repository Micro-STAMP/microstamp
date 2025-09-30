package microstamp.step4new.service;

import microstamp.step4new.dto.refinedscenario.RefinedScenarioCommonCauseReadDto;

import java.util.List;

public interface RefinedScenarioCommonCauseService {

	List<RefinedScenarioCommonCauseReadDto> findAll();

	RefinedScenarioCommonCauseReadDto findByCode(String code);
}