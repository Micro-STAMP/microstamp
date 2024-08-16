package microstamp.step2.service.impl;

import microstamp.step2.dto.connectionaction.ConnectionActionReadDto;
import microstamp.step2.dto.connectionaction.ConnectionActionUpdateDto;
import microstamp.step2.entity.Connection;
import microstamp.step2.entity.ConnectionAction;
import microstamp.step2.dto.connectionaction.ConnectionActionInsertDto;
import microstamp.step2.entity.Environment;
import microstamp.step2.enumeration.ConnectionActionType;
import microstamp.step2.exception.Step2InvalidConnectionActionException;
import microstamp.step2.exception.Step2NotFoundException;
import microstamp.step2.mapper.ConnectionActionMapper;
import microstamp.step2.repository.ConnectionRepository;
import microstamp.step2.repository.ConnectionActionRepository;
import microstamp.step2.service.ConnectionActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Component
public class ConnectionActionServiceImpl implements ConnectionActionService {

    @Autowired
    private ConnectionActionRepository connectionActionRepository;

    @Autowired
    private ConnectionRepository connectionRepository;

    public List<ConnectionActionReadDto> findAll() {
        return connectionActionRepository.findAll().stream()
                .map(ConnectionActionMapper::toDto)
                .sorted(Comparator.comparing(ConnectionActionReadDto::getCode))
                .toList();
    }

    public ConnectionActionReadDto findById(UUID id) throws Step2NotFoundException {
        return ConnectionActionMapper.toDto(connectionActionRepository.findById(id)
                .orElseThrow(() -> new Step2NotFoundException("ConnectionAction", id.toString())));
    }

    public List<ConnectionActionReadDto> findByConnectionId(UUID id) {
        return connectionActionRepository.findByConnectionId(id).stream()
                .map(ConnectionActionMapper::toDto)
                .sorted(Comparator.comparing(ConnectionActionReadDto::getCode))
                .toList();
    }

    public ConnectionActionReadDto insert(ConnectionActionInsertDto connectionActionInsertDto) throws Step2NotFoundException {
        Connection connection = connectionRepository.findById(connectionActionInsertDto.getConnectionId())
                .orElseThrow(() -> new Step2NotFoundException("Connection", connectionActionInsertDto.getConnectionId().toString()));

        validate(connection, connectionActionInsertDto.getConnectionActionType());

        ConnectionAction connectionAction = ConnectionActionMapper.toEntity(connectionActionInsertDto, connection);
        connectionActionRepository.save(connectionAction);

        return ConnectionActionMapper.toDto(connectionAction);
    }

    public void update(UUID id, ConnectionActionUpdateDto connectionActionUpdateDto) throws Step2NotFoundException {
        ConnectionAction connectionAction = connectionActionRepository.findById(id)
                .orElseThrow(() -> new Step2NotFoundException("ConnectionAction", id.toString()));

        validate(connectionAction.getConnection(), connectionActionUpdateDto.getConnectionActionType());

        connectionAction.setName(connectionActionUpdateDto.getName());
        connectionAction.setCode(connectionActionUpdateDto.getCode());
        connectionAction.setConnectionActionType(connectionActionUpdateDto.getConnectionActionType());

        connectionActionRepository.save(connectionAction);
    }

    public void delete(UUID id) throws Step2NotFoundException {
        ConnectionAction connectionAction = connectionActionRepository.findById(id)
                .orElseThrow(() -> new Step2NotFoundException("ConnectionAction", id.toString()));
        connectionActionRepository.deleteById(connectionAction.getId());
    }

    public void validate(Connection connection, ConnectionActionType connectionActionType) {
        if (connection.getSource() instanceof Environment && !ConnectionActionType.getSourceEnvironmentTypes().contains(connectionActionType))
            throw new Step2InvalidConnectionActionException("Invalid connectionActionType for Environment as source: " + connectionActionType +
                    ". Valid types: " + ConnectionActionType.getSourceEnvironmentTypes());

        if (connection.getTarget() instanceof Environment && !ConnectionActionType.getTargetEnvironmentTypes().contains(connectionActionType))
            throw new Step2InvalidConnectionActionException("Invalid connectionActionType for Environment as target: " + connectionActionType +
                    ". Valid types: " + ConnectionActionType.getTargetEnvironmentTypes());

        if (!(connection.getSource() instanceof Environment) && !(connection.getTarget() instanceof Environment)
                && !ConnectionActionType.getDefaultTypes().contains(connectionActionType))
            throw new Step2InvalidConnectionActionException("Invalid connectionActionType for a connection between two components: " +
                    connectionActionType + ". Valid types: " + ConnectionActionType.getDefaultTypes());
    }
}
