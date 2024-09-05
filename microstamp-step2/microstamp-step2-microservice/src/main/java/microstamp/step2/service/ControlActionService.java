package microstamp.step2.service;

import microstamp.step2.dto.controlaction.ControlActionReadDto;

import java.util.List;
import java.util.UUID;

public interface ControlActionService {

    List<ControlActionReadDto> findAll();

    ControlActionReadDto findById(UUID id);

    List<ControlActionReadDto> findByComponentId(UUID id);

    List<ControlActionReadDto> findByConnectionId(UUID id);

}
