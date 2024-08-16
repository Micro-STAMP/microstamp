package microstamp.step2.mapper;

import microstamp.step2.dto.connectionaction.ConnectionActionInsertDto;
import microstamp.step2.dto.connectionaction.ConnectionActionReadDto;
import microstamp.step2.entity.Connection;
import microstamp.step2.entity.ConnectionAction;

public class ConnectionActionMapper {

    public static ConnectionActionReadDto toDto(ConnectionAction connectionAction){
        return ConnectionActionReadDto.builder()
                .id(connectionAction.getId())
                .name(connectionAction.getName())
                .code(connectionAction.getCode())
                .connectionActionType(connectionAction.getConnectionActionType())
                .build();
    }

    public static ConnectionAction toEntity(ConnectionActionInsertDto connectionActionInsertDto, Connection connection){
        return ConnectionAction.builder()
                .name(connectionActionInsertDto.getName())
                .code(connectionActionInsertDto.getCode())
                .connectionActionType(connectionActionInsertDto.getConnectionActionType())
                .connection(connection)
                .build();
    }
}
