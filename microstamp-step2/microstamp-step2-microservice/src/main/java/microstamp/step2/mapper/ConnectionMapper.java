package microstamp.step2.mapper;

import microstamp.step2.dto.connection.ConnectionInsertDto;
import microstamp.step2.dto.connection.ConnectionReadDto;
import microstamp.step2.dto.interaction.InteractionReadDto;
import microstamp.step2.entity.Component;
import microstamp.step2.entity.Connection;

import java.util.Comparator;

public class ConnectionMapper {

    public static ConnectionReadDto toDto(Connection connection){
        return ConnectionReadDto.builder()
                .id(connection.getId())
                .code(connection.getCode())
                .source(ComponentMapper.toDto(connection.getSource()))
                .target(ComponentMapper.toDto(connection.getTarget()))
                .style(connection.getStyle())
                .interactions(connection.getInteractions() != null
                        ? connection.getInteractions().stream()
                            .map(InteractionMapper::toDto)
                            .sorted(Comparator.comparing(InteractionReadDto::getCode))
                            .toList()
                        : null)
                .build();
    }

    public static Connection toEntity(ConnectionInsertDto connectionInsertDto, Component source, Component target){
        return Connection.builder()
                .code(connectionInsertDto.getCode())
                .style(connectionInsertDto.getStyle())
                .analysisId(connectionInsertDto.getAnalysisId())
                .source(source)
                .target(target)
                .build();
    }
}
