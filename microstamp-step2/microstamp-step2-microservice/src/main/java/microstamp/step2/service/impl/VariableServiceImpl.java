package microstamp.step2.service.impl;

import microstamp.step2.dto.variable.VariableReadDto;
import microstamp.step2.dto.variable.VariableUpdateDto;
import microstamp.step2.entity.Variable;
import microstamp.step2.dto.variable.VariableInsertDto;
import microstamp.step2.exception.Step2NotFoundException;
import microstamp.step2.mapper.VariableMapper;
import microstamp.step2.repository.ComponentRepository;
import microstamp.step2.repository.VariableRepository;
import microstamp.step2.service.VariableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Component
public class VariableServiceImpl implements VariableService {

    @Autowired
    private VariableRepository variableRepository;

    @Autowired
    private ComponentRepository componentRepository;

    public List<VariableReadDto> findAll() {
        return variableRepository.findAll().stream()
                .map(VariableMapper::toDto)
                .sorted(Comparator.comparing(VariableReadDto::getCode))
                .toList();
    }

    public VariableReadDto findById(UUID id) throws Step2NotFoundException {
        return VariableMapper.toDto(variableRepository.findById(id)
                .orElseThrow(() -> new Step2NotFoundException("Variable", id.toString())));
    }

    public List<VariableReadDto> findByAnalysisId(UUID id) {
        return componentRepository.findByAnalysisId(id).stream()
                .filter(c -> !c.getVariables().isEmpty())
                .flatMap(c -> variableRepository.findByComponentId(c.getId()).stream())
                .map(VariableMapper::toDto)
                .sorted(Comparator.comparing(VariableReadDto::getCode))
                .toList();
    }

    public List<VariableReadDto> findByComponentId(UUID id) {
        return variableRepository.findByComponentId(id).stream()
                .map(VariableMapper::toDto)
                .sorted(Comparator.comparing(VariableReadDto::getCode))
                .toList();
    }

    public VariableReadDto insert(VariableInsertDto variableInsertDto) throws Step2NotFoundException {
        microstamp.step2.entity.Component component = componentRepository.findById(variableInsertDto.getComponentId())
                .orElseThrow(() -> new Step2NotFoundException("Component", variableInsertDto.getComponentId().toString()));

        Variable variable = VariableMapper.toEntity(variableInsertDto, component);
        variableRepository.save(variable);

        return VariableMapper.toDto(variable);
    }

    public void update(UUID id, VariableUpdateDto variableUpdateDto) throws Step2NotFoundException {
        Variable variable = variableRepository.findById(id)
                .orElseThrow(() -> new Step2NotFoundException("Variable", id.toString()));

        variable.setName(variableUpdateDto.getName());
        variable.setCode(variableUpdateDto.getCode());

        variableRepository.save(variable);
    }

    public void delete(UUID id) throws Step2NotFoundException {
        Variable variable = variableRepository.findById(id)
                .orElseThrow(() -> new Step2NotFoundException("Variable", id.toString()));
        variableRepository.deleteById(variable.getId());
    }
}