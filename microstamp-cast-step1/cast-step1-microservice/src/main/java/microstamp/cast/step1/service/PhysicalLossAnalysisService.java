package microstamp.cast.step1.service;

import microstamp.cast.step1.dto.physicallossanalysis.PhysicalLossAnalysisInsertDto;
import microstamp.cast.step1.dto.physicallossanalysis.PhysicalLossAnalysisReadDto;
import microstamp.cast.step1.dto.physicallossanalysis.PhysicalLossAnalysisUpdateDto;

import java.util.List;
import java.util.UUID;

public interface PhysicalLossAnalysisService {

    List<PhysicalLossAnalysisReadDto> findAll();

    PhysicalLossAnalysisReadDto findById(UUID id);

    List<PhysicalLossAnalysisReadDto> findByAnalysisId(UUID id);

    PhysicalLossAnalysisReadDto insert(PhysicalLossAnalysisInsertDto physicalLossAnalysisInsertDto);

    void update(UUID id, PhysicalLossAnalysisUpdateDto physicalLossAnalysisUpdateDto);

    void delete(UUID id);
}