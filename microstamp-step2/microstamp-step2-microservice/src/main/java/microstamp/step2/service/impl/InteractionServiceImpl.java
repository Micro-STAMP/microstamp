package microstamp.step2.service.impl;

import microstamp.step2.dto.interaction.InteractionReadDto;
import microstamp.step2.dto.interaction.InteractionUpdateDto;
import microstamp.step2.entity.Connection;
import microstamp.step2.entity.Interaction;
import microstamp.step2.dto.interaction.InteractionInsertDto;
import microstamp.step2.entity.Environment;
import microstamp.step2.enumeration.InteractionType;
import microstamp.step2.exception.Step2InvalidInteractionException;
import microstamp.step2.exception.Step2NotFoundException;
import microstamp.step2.mapper.InteractionMapper;
import microstamp.step2.repository.ConnectionRepository;
import microstamp.step2.repository.InteractionRepository;
import microstamp.step2.service.InteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Component
public class InteractionServiceImpl implements InteractionService {

    @Autowired
    private InteractionRepository interactionRepository;

    @Autowired
    private ConnectionRepository connectionRepository;

    public List<InteractionReadDto> findAll() {
        return interactionRepository.findAll().stream()
                .map(InteractionMapper::toDto)
                .sorted(Comparator.comparing(InteractionReadDto::getCode))
                .toList();
    }

    public InteractionReadDto findById(UUID id) throws Step2NotFoundException {
        return InteractionMapper.toDto(interactionRepository.findById(id)
                .orElseThrow(() -> new Step2NotFoundException("Interaction", id.toString())));
    }

    public List<InteractionReadDto> findByConnectionId(UUID id) {
        return interactionRepository.findByConnectionId(id).stream()
                .map(InteractionMapper::toDto)
                .sorted(Comparator.comparing(InteractionReadDto::getCode))
                .toList();
    }

    public InteractionReadDto insert(InteractionInsertDto interactionInsertDto) throws Step2NotFoundException {
        Connection connection = connectionRepository.findById(interactionInsertDto.getConnectionId())
                .orElseThrow(() -> new Step2NotFoundException("Connection", interactionInsertDto.getConnectionId().toString()));

        validate(connection, interactionInsertDto.getInteractionType());

        Interaction interaction = InteractionMapper.toEntity(interactionInsertDto, connection);
        interactionRepository.save(interaction);

        return InteractionMapper.toDto(interaction);
    }

    public void update(UUID id, InteractionUpdateDto interactionUpdateDto) throws Step2NotFoundException {
        Interaction interaction = interactionRepository.findById(id)
                .orElseThrow(() -> new Step2NotFoundException("Interaction", id.toString()));

        validate(interaction.getConnection(), interactionUpdateDto.getInteractionType());

        interaction.setName(interactionUpdateDto.getName());
        interaction.setCode(interactionUpdateDto.getCode());
        interaction.setInteractionType(interactionUpdateDto.getInteractionType());

        interactionRepository.save(interaction);
    }

    public void delete(UUID id) throws Step2NotFoundException {
        Interaction interaction = interactionRepository.findById(id)
                .orElseThrow(() -> new Step2NotFoundException("Interaction", id.toString()));
        interactionRepository.deleteById(interaction.getId());
    }

    public void validate(Connection connection, InteractionType interactionType) {
        if (connection.getSource() instanceof Environment && !InteractionType.getSourceEnvironmentTypes().contains(interactionType))
            throw new Step2InvalidInteractionException("Invalid interactionType for Environment as source: " + interactionType +
                    ". Valid types: " + InteractionType.getSourceEnvironmentTypes());

        if (connection.getTarget() instanceof Environment && !InteractionType.getTargetEnvironmentTypes().contains(interactionType))
            throw new Step2InvalidInteractionException("Invalid interactionType for Environment as target: " + interactionType +
                    ". Valid types: " + InteractionType.getTargetEnvironmentTypes());

        if (!(connection.getSource() instanceof Environment) && !(connection.getTarget() instanceof Environment)
                && !InteractionType.getDefaultTypes().contains(interactionType))
            throw new Step2InvalidInteractionException("Invalid interactionType for a connection between two components: " +
                    interactionType + ". Valid types: " + InteractionType.getDefaultTypes());
    }
}
