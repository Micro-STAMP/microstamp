package microstamp.step2.service.impl;

import lombok.RequiredArgsConstructor;
import microstamp.step2.dto.state.StateReadDto;
import microstamp.step2.dto.state.StateUpdateDto;
import microstamp.step2.entity.State;
import microstamp.step2.entity.Variable;
import microstamp.step2.dto.state.StateInsertDto;
import microstamp.step2.exception.Step2NotFoundException;
import microstamp.step2.mapper.StateMapper;
import microstamp.step2.repository.StateRepository;
import microstamp.step2.repository.VariableRepository;
import microstamp.step2.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StateServiceImpl implements StateService {

    private final StateRepository stateRepository;

    private final VariableRepository variableRepository;

    public List<StateReadDto> findAll() {
        return stateRepository.findAll().stream()
                .map(StateMapper::toDto)
                .sorted(Comparator.comparing(StateReadDto::getCode))
                .toList();
    }

    public StateReadDto findById(UUID id) throws Step2NotFoundException {
        return StateMapper.toDto(stateRepository.findById(id)
                .orElseThrow(() -> new Step2NotFoundException("State", id.toString())));
    }

    public StateReadDto insert(StateInsertDto stateInsertDto) throws Step2NotFoundException {
        Variable variable = variableRepository.findById(stateInsertDto.getVariableId())
                .orElseThrow(() -> new Step2NotFoundException("Variable", stateInsertDto.getVariableId().toString()));

        State state = StateMapper.toEntity(stateInsertDto, variable);
        stateRepository.save(state);

        return StateMapper.toDto(state);
    }

    public void update(UUID id, StateUpdateDto stateUpdateDto) throws Step2NotFoundException {
        State state = stateRepository.findById(id)
                .orElseThrow(() -> new Step2NotFoundException("State", id.toString()));

        state.setName(stateUpdateDto.getName());
        state.setCode(stateUpdateDto.getCode());

        stateRepository.save(state);
    }

    public void delete(UUID id) throws Step2NotFoundException {
        State state = stateRepository.findById(id)
                .orElseThrow(() -> new Step2NotFoundException("State", id.toString()));
        stateRepository.deleteById(state.getId());
    }
}
