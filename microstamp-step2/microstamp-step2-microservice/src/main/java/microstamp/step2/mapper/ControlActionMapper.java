package microstamp.step2.mapper;

import microstamp.step2.dto.controlaction.ControlActionReadDto;
import microstamp.step2.entity.ConnectionAction;

public class ControlActionMapper {

    public static ControlActionReadDto toDto(ConnectionAction connectionAction) {
        return ControlActionReadDto.builder()
                .id(connectionAction.getId())
                .name(connectionAction.getName())
                .code(connectionAction.getCode())
                .build();
    }
}
