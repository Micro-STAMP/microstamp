package microstamp.step2.service;

import microstamp.step2.dto.variable.VariableInsertDto;
import microstamp.step2.dto.variable.VariableReadDto;
import microstamp.step2.dto.variable.VariableUpdateDto;

import java.util.List;
import java.util.UUID;

public interface VariableService {

    List<VariableReadDto> findAll();

    VariableReadDto findById(UUID id);

    List<VariableReadDto> findByAnalysisId(UUID id);

    List<VariableReadDto> findByComponentId(UUID id);

    VariableReadDto insert(VariableInsertDto variableInsertDto);

    void update(UUID id, VariableUpdateDto variableUpdateDto);

    void delete(UUID id);

}
