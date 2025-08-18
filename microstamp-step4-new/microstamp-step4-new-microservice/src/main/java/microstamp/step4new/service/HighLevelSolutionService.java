package microstamp.step4new.service;

import microstamp.step4new.dto.highlevelsolution.HighLevelSolutionInsertDto;
import microstamp.step4new.dto.highlevelsolution.HighLevelSolutionReadDto;
import microstamp.step4new.dto.highlevelsolution.HighLevelSolutionUpdateDto;

import java.util.List;
import java.util.UUID;

public interface HighLevelSolutionService {

    HighLevelSolutionReadDto findById(UUID id);

    HighLevelSolutionReadDto getOrCreateByFormalScenarioClassId(UUID formalScenarioClassId);

    List<HighLevelSolutionReadDto> getOrCreateByUnsafeControlActionId(UUID unsafeControlActionId);

    HighLevelSolutionReadDto create(HighLevelSolutionInsertDto dto);

    void update(UUID id, HighLevelSolutionUpdateDto dto);

    void delete(UUID id);
}
