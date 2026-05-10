package microstamp.cast.step1.service;

import microstamp.cast.step1.dto.casthazard.CastHazardInsertDto;
import microstamp.cast.step1.dto.casthazard.CastHazardReadDto;
import microstamp.cast.step1.dto.casthazard.CastHazardUpdateDto;

import java.util.List;
import java.util.UUID;

public interface CastHazardService {

    List<CastHazardReadDto> findAll();

    CastHazardReadDto findById(UUID id);

    List<CastHazardReadDto> findByAnalysisId(UUID id);

    CastHazardReadDto insert(CastHazardInsertDto castHazardInsertDto);

    void update(UUID id, CastHazardUpdateDto castHazardUpdateDto);

    void delete(UUID id);
}