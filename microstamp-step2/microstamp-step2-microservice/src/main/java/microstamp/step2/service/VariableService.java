package microstamp.step2.service;

import microstamp.step2.dto.variable.VariableInsertDto;
import microstamp.step2.dto.variable.VariableFullReadDto;
import microstamp.step2.dto.variable.VariableUpdateDto;

import java.util.List;
import java.util.UUID;

public interface VariableService {

    List<VariableFullReadDto> findAll();

    VariableFullReadDto findById(UUID id);

    List<VariableFullReadDto> findByAnalysisId(UUID id);

    List<VariableFullReadDto> findByComponentId(UUID id);

    VariableFullReadDto insert(VariableInsertDto variableInsertDto);

    void update(UUID id, VariableUpdateDto variableUpdateDto);

    void delete(UUID id);

}
