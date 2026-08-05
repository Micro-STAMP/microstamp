package microstamp.cast.step4.service;

import microstamp.cast.step4.dto.systemicfactor.SystemicFactorInsertDto;
import microstamp.cast.step4.dto.systemicfactor.SystemicFactorReadDto;
import microstamp.cast.step4.dto.systemicfactor.SystemicFactorUpdateDto;

import java.util.List;
import java.util.UUID;

public interface SystemicFactorService {
    SystemicFactorReadDto create(SystemicFactorInsertDto dto);
    SystemicFactorReadDto findById(UUID id);
    List<SystemicFactorReadDto> findByAnalysisId(UUID analysisId);
    SystemicFactorReadDto update(UUID id, SystemicFactorUpdateDto dto);
    void delete(UUID id);
}
