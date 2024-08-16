package microstamp.step2.service;

import microstamp.step2.dto.connectionaction.ConnectionActionInsertDto;
import microstamp.step2.dto.connectionaction.ConnectionActionReadDto;
import microstamp.step2.dto.connectionaction.ConnectionActionUpdateDto;
import microstamp.step2.entity.Connection;
import microstamp.step2.enumeration.ConnectionActionType;

import java.util.List;
import java.util.UUID;

public interface ConnectionActionService {

    List<ConnectionActionReadDto> findAll();

    ConnectionActionReadDto findById(UUID id);

    List<ConnectionActionReadDto> findByConnectionId(UUID id);

    ConnectionActionReadDto insert(ConnectionActionInsertDto connectionActionInsertDto);

    void update(UUID id, ConnectionActionUpdateDto connectionActionUpdateDto);

    void delete(UUID id);

    void validate(Connection connection, ConnectionActionType connectionActionType);

}
