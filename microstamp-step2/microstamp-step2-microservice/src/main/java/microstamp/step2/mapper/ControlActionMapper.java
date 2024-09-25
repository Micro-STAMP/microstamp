package microstamp.step2.mapper;

import microstamp.step2.dto.controlaction.ControlActionReadDto;
import microstamp.step2.entity.Interaction;

public class ControlActionMapper {

    public static ControlActionReadDto toDto(Interaction interaction) {
        return ControlActionReadDto.builder()
                .id(interaction.getId())
                .name(interaction.getName())
                .code(interaction.getCode())
                .connection(ConnectionMapper.toDto(interaction.getConnection()))
                .build();
    }
}
