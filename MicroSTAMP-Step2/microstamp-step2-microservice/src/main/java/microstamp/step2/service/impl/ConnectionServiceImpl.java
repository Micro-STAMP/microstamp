package microstamp.step2.service.impl;

import microstamp.step2.client.MicroStampAuthClient;
import microstamp.step2.dto.connection.ConnectionReadDto;
import microstamp.step2.dto.connection.ConnectionUpdateDto;
import microstamp.step2.entity.Connection;
import microstamp.step2.dto.connection.ConnectionInsertDto;
import microstamp.step2.exception.Step2NotFoundException;
import microstamp.step2.mapper.ConnectionMapper;
import microstamp.step2.repository.ComponentRepository;
import microstamp.step2.repository.ConnectionRepository;
import microstamp.step2.service.ConnectionActionService;
import microstamp.step2.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Component
public class ConnectionServiceImpl implements ConnectionService {

    @Autowired
    private MicroStampAuthClient microStampAuthClient;

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private ComponentRepository componentRepository;

    @Autowired
    private ConnectionActionService connectionActionService;

    public List<ConnectionReadDto> findAll() {
        return connectionRepository.findAll().stream()
                .map(ConnectionMapper::toDto)
                .sorted(Comparator.comparing(ConnectionReadDto::getCode))
                .toList();
    }

    public ConnectionReadDto findById(UUID id) throws Step2NotFoundException {
        return ConnectionMapper.toDto(connectionRepository.findById(id)
                .orElseThrow(() -> new Step2NotFoundException("Connection", id.toString())));
    }

    public List<ConnectionReadDto> findByAnalysisId(UUID id) {
        return connectionRepository.findByAnalysisId(id).stream()
                .map(ConnectionMapper::toDto)
                .sorted(Comparator.comparing(ConnectionReadDto::getCode))
                .toList();
    }

    public ConnectionReadDto insert(ConnectionInsertDto connectionInsertDto) throws Step2NotFoundException {
        microStampAuthClient.getAnalysisById(connectionInsertDto.getAnalysisId());

        microstamp.step2.entity.Component source = componentRepository.findById(connectionInsertDto.getSourceId())
                .orElseThrow(() -> new Step2NotFoundException("Source Component", connectionInsertDto.getSourceId().toString()));

        microstamp.step2.entity.Component target = componentRepository.findById(connectionInsertDto.getTargetId())
                .orElseThrow(() -> new Step2NotFoundException("Target Component", connectionInsertDto.getTargetId().toString()));

        Connection connection = ConnectionMapper.toEntity(connectionInsertDto, source, target);
        connectionRepository.save(connection);

        return ConnectionMapper.toDto(connection);
    }

    public void update(UUID id, ConnectionUpdateDto connectionUpdateDto) throws Step2NotFoundException {
        Connection connection = connectionRepository.findById(id)
                .orElseThrow(() -> new Step2NotFoundException("Connection", id.toString()));

        microstamp.step2.entity.Component source = componentRepository.findById(connectionUpdateDto.getSourceId())
                .orElseThrow(() -> new Step2NotFoundException("Source Component", connectionUpdateDto.getSourceId().toString()));

        microstamp.step2.entity.Component target = componentRepository.findById(connectionUpdateDto.getTargetId())
                .orElseThrow(() -> new Step2NotFoundException("Target Component", connectionUpdateDto.getTargetId().toString()));

        connection.setCode(connectionUpdateDto.getCode());
        connection.setSource(source);
        connection.setTarget(target);
        connection.setStyle(connectionUpdateDto.getStyle());

        connection.getConnectionActions().forEach(connectionAction ->
                connectionActionService.validate(connection, connectionAction.getConnectionActionType()));

        connectionRepository.save(connection);
    }

    public void delete(UUID id) throws Step2NotFoundException {
        Connection connection = connectionRepository.findById(id)
                .orElseThrow(() -> new Step2NotFoundException("Connection", id.toString()));

        connectionRepository.deleteById(connection.getId());
    }

}
