package microstamp.cast.step3.service;

import microstamp.cast.step3.dto.inadequatecontrolaction.InadequateControlActionInsertDto;
import microstamp.cast.step3.dto.inadequatecontrolaction.InadequateControlActionReadDto;
import microstamp.cast.step3.dto.inadequatecontrolaction.InadequateControlActionUpdateDto;
import java.util.List;
import java.util.UUID;

public interface InadequateControlActionService {
    InadequateControlActionReadDto create(InadequateControlActionInsertDto dto);
    List<InadequateControlActionReadDto> findByAnalysisId(UUID analysisId);
    InadequateControlActionReadDto update(UUID id, InadequateControlActionUpdateDto dto);
    void delete(UUID id);
}