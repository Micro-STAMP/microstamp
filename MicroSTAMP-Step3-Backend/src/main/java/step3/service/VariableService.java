package step3.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import step3.dto.variable.VariableCreateDto;
import step3.dto.variable.*;
import step3.entity.Variable;
import step3.repository.ControllerRepository;
import step3.repository.RuleRepository;
import step3.repository.UnsafeControlActionRepository;
import step3.repository.VariableRepository;
import java.util.List;

@Service
@AllArgsConstructor
public class VariableService {
    private final VariableRepository variableRepository;
    private final ControllerRepository controllerRepository;
    private final RuleRepository ruleRepository;
    private final UnsafeControlActionRepository ucaRepository;

    // Create -----------------------------------------

    public VariableReadDto createVariable(VariableCreateDto variableCreateDto) {
        Variable variable = new Variable(variableCreateDto.name(), controllerRepository.getReferenceById(variableCreateDto.controller_id()));
        Variable createdVariable = variableRepository.save(variable);
        return new VariableReadDto(createdVariable);
    }

    // Read -------------------------------------------

    public VariableReadDto readVariable(Long id) {
        Variable variable = variableRepository.getReferenceById(id);
        return new VariableReadDto(variable);
    }
    public List<VariableReadListDto> readAllVariables() {
        return variableRepository.findAll().stream().map(VariableReadListDto::new).toList();
    }

    public List<VariableReadListDto> readVariablesByControllerId(Long controllerId) {
        return variableRepository.findByControllerId(controllerId).stream().map(VariableReadListDto::new).toList();
    }

    // Update -----------------------------------------

    public VariableReadDto updateVariable(Long id, VariableUpdateDto variableDto) {
        Variable updatedVariable = variableRepository.getReferenceById(id);
        updatedVariable.setName(variableDto.name());
        return new VariableReadDto(variableRepository.save(updatedVariable));
    }

    // Delete -----------------------------------------

    public void deleteVariable(Long id) {
        var variableToBeDeleted = variableRepository.getReferenceById(id);
        var controller = variableToBeDeleted.getController();

        controller.setContextTable(null);
        controllerRepository.save(controller);

        ruleRepository.deleteAll();
        ucaRepository.deleteAll();
        variableRepository.deleteById(id);
    }

    // Methods ----------------------------------------

    // ------------------------------------------------
}

