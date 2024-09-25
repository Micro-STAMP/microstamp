package microstamp.step2.mapper;

import microstamp.step2.dto.interaction.InteractionInsertDto;
import microstamp.step2.dto.interaction.InteractionReadDto;
import microstamp.step2.entity.Connection;
import microstamp.step2.entity.Interaction;

public class InteractionMapper {

    public static InteractionReadDto toDto(Interaction interaction){
        return InteractionReadDto.builder()
                .id(interaction.getId())
                .name(interaction.getName())
                .code(interaction.getCode())
                .interactionType(interaction.getInteractionType())
                .build();
    }

    public static Interaction toEntity(InteractionInsertDto interactionInsertDto, Connection connection){
        return Interaction.builder()
                .name(interactionInsertDto.getName())
                .code(interactionInsertDto.getCode())
                .interactionType(interactionInsertDto.getInteractionType())
                .connection(connection)
                .build();
    }
}
