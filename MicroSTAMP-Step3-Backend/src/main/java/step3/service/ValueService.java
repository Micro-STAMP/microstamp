package step3.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import step3.dto.value.ValueCreateDto;
import step3.dto.value.ValueReadDto;
import step3.entity.Value;
import step3.entity.Variable;
import step3.repository.*;

import java.util.List;

@Service
@AllArgsConstructor
public class ValueService {
    private final ValueRepository valueRepository;
    private final VariableRepository variableRepository;
    private final ControllerRepository controllerRepository;
    private final RuleRepository ruleRepository;
    private final UnsafeControlActionRepository ucaRepository;

    // Create -----------------------------------------

    public ValueReadDto createValue(ValueCreateDto valueCreateDto) {
        Variable variable = variableRepository.getReferenceById(valueCreateDto.variable_id());
        Value value = new Value(valueCreateDto.name(), variable);
        Value createdValue = valueRepository.save(value);
        return new ValueReadDto(createdValue);
    }

    // Read -------------------------------------------

    public ValueReadDto readValue(Long id) {
        return new ValueReadDto(valueRepository.getReferenceById(id));
    }
    public List<ValueReadDto> readAllValues() {
        return valueRepository.findAll().stream().map(ValueReadDto::new).toList();
    }

    // Update -----------------------------------------

    public void updateValue(Value value) {
        Value updatedValue = valueRepository.getReferenceById(value.getId());
        updatedValue.setName(value.getName());
        updatedValue.setVariable(value.getVariable());
    }

    // Delete -----------------------------------------

    public void deleteValue(Long id) {
        var value = valueRepository.getReferenceById(id);
        var controller = value.getVariable().getController();

        controller.setContextTable(null);
        controllerRepository.save(controller);

        ruleRepository.deleteAll();
        ucaRepository.deleteAll();
        valueRepository.deleteById(id);
    }

    // Methods ----------------------------------------

    // ------------------------------------------------
}
