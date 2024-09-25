package microstamp.step2.service.impl;

import microstamp.step2.dto.controlaction.ControlActionReadDto;
import microstamp.step2.entity.Connection;
import microstamp.step2.enumeration.InteractionType;
import microstamp.step2.exception.Step2NotFoundException;
import microstamp.step2.mapper.ControlActionMapper;
import microstamp.step2.repository.ComponentRepository;
import microstamp.step2.repository.InteractionRepository;
import microstamp.step2.repository.ConnectionRepository;
import microstamp.step2.service.ControlActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Component
public class ControlActionServiceImpl implements ControlActionService {

    @Autowired
    private ComponentRepository componentRepository;

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private InteractionRepository interactionRepository;

    public List<ControlActionReadDto> findAll() {
        return interactionRepository.findByInteractionType(InteractionType.CONTROL_ACTION).stream()
                .map(ControlActionMapper::toDto)
                .sorted(Comparator.comparing(ControlActionReadDto::getCode))
                .toList();
    }

    public ControlActionReadDto findById(UUID id) throws Step2NotFoundException {
        return ControlActionMapper.toDto(interactionRepository.findById(id)
                .filter(c -> c.getInteractionType() == InteractionType.CONTROL_ACTION)
                .orElseThrow(() -> new Step2NotFoundException("ControlAction", id.toString())));
    }

    public List<ControlActionReadDto> findByComponentId(UUID id) {
        List<Connection> connections = connectionRepository.findBySourceId(id);
        return connections.stream()
                .flatMap(connection -> connection.getInteractions().stream()
                        .filter(c -> c.getInteractionType() == InteractionType.CONTROL_ACTION)
                        .map(ControlActionMapper::toDto))
                .sorted(Comparator.comparing(ControlActionReadDto::getCode))
                .toList();
    }

    public List<ControlActionReadDto> findByConnectionId(UUID id) {
        return interactionRepository.findByConnectionId(id)
                .stream()
                .filter(c -> c.getInteractionType() == InteractionType.CONTROL_ACTION)
                .map(ControlActionMapper::toDto)
                .sorted(Comparator.comparing(ControlActionReadDto::getCode))
                .toList();
    }
}
