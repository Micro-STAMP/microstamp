package microstamp.step2.service;

import microstamp.step2.dto.connection.ConnectionInsertDto;
import microstamp.step2.dto.connection.ConnectionReadDto;
import microstamp.step2.dto.connection.ConnectionUpdateDto;

import java.util.List;
import java.util.UUID;

public interface ConnectionService {

    List<ConnectionReadDto> findAll();

    ConnectionReadDto findById(UUID id);

    List<ConnectionReadDto> findByAnalysisId(UUID id);

    ConnectionReadDto insert(ConnectionInsertDto connectionInsertDto);

    void update(UUID id, ConnectionUpdateDto connectionUpdateDto);

    void delete(UUID id);

}
