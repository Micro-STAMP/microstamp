package microstamp.step4new.service;

import microstamp.step4new.dto.mitigation.MitigationInsertDto;
import microstamp.step4new.dto.mitigation.MitigationReadDto;
import microstamp.step4new.dto.mitigation.MitigationUpdateDto;

import java.util.List;
import java.util.UUID;

public interface MitigationService {

	MitigationReadDto findById(UUID id);

	MitigationReadDto getOrCreateByRefinedScenarioId(UUID refinedScenarioId);

	List<MitigationReadDto> findByUnsafeControlActionId(UUID unsafeControlActionId);

	MitigationReadDto create(MitigationInsertDto dto);

	void update(UUID id, MitigationUpdateDto dto);

	void delete(UUID id);
}


