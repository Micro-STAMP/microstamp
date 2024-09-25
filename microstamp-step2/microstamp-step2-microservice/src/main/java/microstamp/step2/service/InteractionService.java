package microstamp.step2.service;

import microstamp.step2.dto.interaction.InteractionInsertDto;
import microstamp.step2.dto.interaction.InteractionReadDto;
import microstamp.step2.dto.interaction.InteractionUpdateDto;
import microstamp.step2.entity.Connection;
import microstamp.step2.enumeration.InteractionType;

import java.util.List;
import java.util.UUID;

public interface InteractionService {

    List<InteractionReadDto> findAll();

    InteractionReadDto findById(UUID id);

    List<InteractionReadDto> findByConnectionId(UUID id);

    InteractionReadDto insert(InteractionInsertDto interactionInsertDto);

    void update(UUID id, InteractionUpdateDto interactionUpdateDto);

    void delete(UUID id);

    void validate(Connection connection, InteractionType interactionType);

}
