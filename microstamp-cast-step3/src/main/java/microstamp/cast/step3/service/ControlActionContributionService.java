package microstamp.cast.step3.service;

import microstamp.cast.step3.dto.controlactioncontribution.ControlActionContributionInsertDto;
import microstamp.cast.step3.dto.controlactioncontribution.ControlActionContributionReadDto;
import microstamp.cast.step3.dto.controlactioncontribution.ControlActionContributionUpdateDto;
import java.util.List;
import java.util.UUID;

public interface ControlActionContributionService {
    ControlActionContributionReadDto create(ControlActionContributionInsertDto dto);
    List<ControlActionContributionReadDto> findByAnalysisId(UUID analysisId);
    ControlActionContributionReadDto findById(UUID id);
    ControlActionContributionReadDto update(UUID id, ControlActionContributionUpdateDto dto);
    void delete(UUID id);
}
