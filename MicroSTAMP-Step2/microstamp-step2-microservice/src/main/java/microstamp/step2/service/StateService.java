package microstamp.step2.service;

import microstamp.step2.dto.state.StateInsertDto;
import microstamp.step2.dto.state.StateReadDto;
import microstamp.step2.dto.state.StateUpdateDto;

import java.util.List;
import java.util.UUID;

public interface StateService {

    List<StateReadDto> findAll();

    StateReadDto findById(UUID id);

    StateReadDto insert(StateInsertDto stateInsertDto);

    void update(UUID id, StateUpdateDto stateUpdateDto);

    void delete(UUID id);

}
