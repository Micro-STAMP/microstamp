package microstamp.step2.unit;

import microstamp.step2.client.MicroStampAuthClient;
import microstamp.step2.dto.connection.ConnectionInsertDto;
import microstamp.step2.dto.connection.ConnectionReadDto;
import microstamp.step2.dto.connection.ConnectionUpdateDto;
import microstamp.step2.entity.Connection;
import microstamp.step2.entity.ControlledProcess;
import microstamp.step2.entity.Controller;
import microstamp.step2.enumeration.Style;
import microstamp.step2.exception.Step2NotFoundException;
import microstamp.step2.repository.ComponentRepository;
import microstamp.step2.repository.ConnectionRepository;
import microstamp.step2.service.InteractionService;
import microstamp.step2.service.impl.ConnectionServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConnectionServiceUnitTest {

    @InjectMocks
    private ConnectionServiceImpl service;

    @Mock
    private MicroStampAuthClient microStampAuthClient;

    @Mock
    private ConnectionRepository connectionRepository;

    @Mock
    private ComponentRepository componentRepository;

    @Mock
    private InteractionService interactionService;

    @Test
    @DisplayName("#findAll > When no connection are found > Return an empty list")
    void findAllWhenNoConnectionAreFoundReturnAnEmptyList() {
        when(connectionRepository.findAll()).thenReturn(List.of());

        assertTrue(service.findAll().isEmpty());
    }

    @Test
    @DisplayName("#findAll > When the connections are found > Return the connection")
    void findAllWhenTheConnectionsAreFoundReturnTheConnections() {
        Connection firstConnection = assembleConnection.get();
        Connection secondConnection = assembleConnection.get();

        when(connectionRepository.findAll()).thenReturn(List.of(firstConnection, secondConnection));

        List<ConnectionReadDto> response = service.findAll();

        assertAll(
                () -> assertEquals(2, response.size()),
                () -> assertEquals(firstConnection.getId(), response.getFirst().getId()),
                () -> assertEquals(secondConnection.getId(), response.getLast().getId())
        );
    }

    @Test
    @DisplayName("#findById > When no connection is found > Throw an exception")
    void findByIdWhenNoConnectionIsFoundThrowAnException() {
        UUID connectionUUID = UUID.randomUUID();
        when(connectionRepository.findById(connectionUUID)).thenReturn(Optional.empty());

        assertThrows(Step2NotFoundException.class, () -> service.findById(connectionUUID));
    }

    @Test
    @DisplayName("#findById > When the connection is found > Return it")
    void findByIdWhenTheConnectionIsFoundReturnIt() {
        Connection mockConnection = assembleConnection.get();

        when(connectionRepository.findById(mockConnection.getId())).thenReturn(Optional.of(mockConnection));

        ConnectionReadDto response = service.findById(mockConnection.getId());
        assertEquals(mockConnection.getId(), response.getId());
    }

    @Test
    @DisplayName("#findByAnalysisId > When no connection are found > Return an empty list")
    void findByAnalysisIdWhenNoConnectionAreFoundReturnAnEmptyList() {
        UUID analysisUUID = UUID.randomUUID();
        when(connectionRepository.findByAnalysisId(analysisUUID)).thenReturn(List.of());
        assertTrue(service.findByAnalysisId(analysisUUID).isEmpty());
    }

    @Test
    @DisplayName("#findByAnalysisId > When the connections are found > Return the connection")
    void findByAnalysisIdWhenTheConnectionsAreFoundReturnTheConnections() {
        UUID analysisUUID = UUID.randomUUID();
        Connection firstConnection = assembleConnection.get();
        Connection secondConnection = assembleConnection.get();

        when(connectionRepository.findByAnalysisId(analysisUUID)).thenReturn(List.of(firstConnection, secondConnection));

        List<ConnectionReadDto> response = service.findByAnalysisId(analysisUUID);

        assertAll(
                () -> assertEquals(2, response.size()),
                () -> assertEquals(firstConnection.getId(), response.getFirst().getId()),
                () -> assertEquals(secondConnection.getId(), response.getLast().getId())
        );
    }

    @Test
    @DisplayName("#insert > When the associated component source is not found > Throw an exception ")
    void insertWhenTheAssociatedComponentSourceIsNotFoundThrowAnException() {
        ConnectionInsertDto mockConnectionInsert = assembleConnectionInsert.get();
        when(componentRepository.findById(mockConnectionInsert.getSourceId())).thenReturn(Optional.empty());

        assertThrows(Step2NotFoundException.class, () -> service.insert(mockConnectionInsert));
    }

    @Test
    @DisplayName("#insert > When the associated component target is not found > Throw an exception ")
    void insertWhenTheAssociatedComponentTargetIsNotFoundThrowAnException() {
        ConnectionInsertDto mockConnectionInsert = assembleConnectionInsert.get();
        Connection mockConnection = assembleConnection.get();

        when(componentRepository.findById(mockConnectionInsert.getSourceId())).thenReturn(Optional.of(mockConnection.getSource()));
        when(componentRepository.findById(mockConnectionInsert.getTargetId())).thenReturn(Optional.empty());

        assertThrows(Step2NotFoundException.class, () -> service.insert(mockConnectionInsert));
    }

    @Test
    @DisplayName("#insert > When the associated component is found > Insert the variable on database")
    void insertWhenTheAssociatedComponentIsFoundInsertTheVariableOnDatabase() {
        ConnectionInsertDto mockConnectionInsert = assembleConnectionInsert.get();
        Connection mockConnection = assembleConnection.get();

        when(componentRepository.findById(mockConnectionInsert.getSourceId())).thenReturn(Optional.of(mockConnection.getSource()));
        when(componentRepository.findById(mockConnectionInsert.getTargetId())).thenReturn(Optional.of(mockConnection.getTarget()));

        ConnectionReadDto response = service.insert(mockConnectionInsert);

        assertAll(
                () -> assertEquals(mockConnectionInsert.getCode(), response.getCode()),
                () -> verify(connectionRepository, times(1)).save(any())
        );
    }

    @Test
    @DisplayName("#update > When the connection is not found > Throw an exception ")
    void updateWhenTheConnectionIsNotFoundThrowAnException() {
        UUID connectionUUID = UUID.randomUUID();
        ConnectionUpdateDto mockConnectionUpdate = assembleConnectionUpdate.get();

        when(connectionRepository.findById(connectionUUID)).thenReturn(Optional.empty());

        assertThrows(Step2NotFoundException.class, () -> service.update(connectionUUID,mockConnectionUpdate));
    }

    @Test
    @DisplayName("#update > When the associated component source is not found > Throw an exception ")
    void updateWhenTheAssociatedComponentSourceIsNotFoundThrowAnException() {
        UUID connectionUUID = UUID.randomUUID();
        Connection mockConnection = assembleConnection.get();
        ConnectionUpdateDto mockConnectionUpdate = assembleConnectionUpdate.get();

        when(connectionRepository.findById(connectionUUID)).thenReturn(Optional.of(mockConnection));
        when(componentRepository.findById(mockConnectionUpdate.getSourceId())).thenReturn(Optional.empty());

        assertThrows(Step2NotFoundException.class, () -> service.update(connectionUUID,mockConnectionUpdate));
    }

    @Test
    @DisplayName("#update > When the associated component target is not found > Throw an exception ")
    void updateWhenTheAssociatedComponentTargetIsNotFoundThrowAnException() {
        UUID connectionUUID = UUID.randomUUID();
        Connection mockConnection = assembleConnection.get();
        ConnectionUpdateDto mockConnectionUpdate = assembleConnectionUpdate.get();

        when(connectionRepository.findById(connectionUUID)).thenReturn(Optional.of(mockConnection));
        when(componentRepository.findById(mockConnectionUpdate.getSourceId())).thenReturn(Optional.of(mockConnection.getSource()));
        when(componentRepository.findById(mockConnectionUpdate.getTargetId())).thenReturn(Optional.empty());

        assertThrows(Step2NotFoundException.class, () -> service.update(connectionUUID,mockConnectionUpdate));
    }

    @Test
    @DisplayName("#update > When the connection and associated component target and source is found > Update the connection ")
    void updateWhenTheConnectionAndAssociatedComponentTargetAndSourceIsFoundUpdateTheConnection() {
        Connection mockConnection = assembleConnection.get();
        ConnectionUpdateDto mockConnectionUpdate = assembleConnectionUpdate.get();

        when(connectionRepository.findById(mockConnection.getId())).thenReturn(Optional.of(mockConnection));
        when(componentRepository.findById(mockConnectionUpdate.getSourceId())).thenReturn(Optional.of(mockConnection.getSource()));
        when(componentRepository.findById(mockConnectionUpdate.getTargetId())).thenReturn(Optional.of(mockConnection.getTarget()));

        service.update(mockConnection.getId(), mockConnectionUpdate);

        mockConnection.setCode(mockConnectionUpdate.getCode());
        mockConnection.setStyle(mockConnectionUpdate.getStyle());

        verify(connectionRepository, times(1)).save(mockConnection);
    }

    @Test
    @DisplayName("#delete > When the connection is not found > Throw an exception")
    void deleteWhenTheConnectionIsNotFoundThrowAnException() {
        UUID connectionUUID = UUID.randomUUID();
        when(connectionRepository.findById(connectionUUID)).thenReturn(Optional.empty());

        assertThrows(Step2NotFoundException.class, () -> service.delete(connectionUUID));
    }

    @Test
    @DisplayName("#delete > When the connection is found > Delete it")
    void deleteWhenTheConnectionIsFoundDeleteIt() {
        Connection mockConnection = assembleConnection.get();

        when(connectionRepository.findById(mockConnection.getId())).thenReturn(Optional.of(mockConnection));

        service.delete(mockConnection.getId());
        verify(connectionRepository, times(1)).deleteById(mockConnection.getId());
    }

    private final Supplier<Connection> assembleConnection = () -> Connection.builder()
            .id(UUID.randomUUID())
            .code("Code")
            .source(new Controller())
            .target(new ControlledProcess())
            .interactions(List.of())
            .build();

    private final Supplier<ConnectionInsertDto> assembleConnectionInsert = () -> ConnectionInsertDto.builder()
            .code("Code")
            .sourceId(UUID.randomUUID())
            .targetId(UUID.randomUUID())
            .build();

    private final Supplier<ConnectionUpdateDto> assembleConnectionUpdate = () -> ConnectionUpdateDto.builder()
            .code("Code")
            .sourceId(UUID.randomUUID())
            .targetId(UUID.randomUUID())
            .style(Style.SOLID)
            .build();

}