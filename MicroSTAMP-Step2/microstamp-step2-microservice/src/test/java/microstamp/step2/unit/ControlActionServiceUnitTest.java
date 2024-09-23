package microstamp.step2.unit;

import microstamp.step2.dto.controlaction.ControlActionReadDto;
import microstamp.step2.entity.Connection;
import microstamp.step2.entity.ConnectionAction;
import microstamp.step2.entity.Controller;
import microstamp.step2.enumeration.ConnectionActionType;
import microstamp.step2.exception.Step2NotFoundException;
import microstamp.step2.repository.ConnectionActionRepository;
import microstamp.step2.repository.ConnectionRepository;
import microstamp.step2.service.impl.ControlActionServiceImpl;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ControlActionServiceUnitTest {

    @InjectMocks
    private ControlActionServiceImpl service;

    @Mock
    private ConnectionRepository connectionRepository;

    @Mock
    private ConnectionActionRepository connectionActionRepository;

    @Test
    @DisplayName("#findAll > When no connection action are found > Return an empty list")
    void findAllWhenNoConnectionActionAreFoundReturnAnEmptyList() {
        when(connectionActionRepository.findByConnectionActionType(ConnectionActionType.CONTROL_ACTION)).thenReturn(List.of());

        assertTrue(service.findAll().isEmpty());
    }

    @Test
    @DisplayName("#findAll > When the connection actions are found > Return it")
    void findAllWhenTheConnectionActionsAreFoundReturnIt() {
        ConnectionAction firstConnectionAction = assembleConnectionAction.get();
        ConnectionAction secondConnectionAction = assembleConnectionAction.get();

        when(connectionActionRepository.findByConnectionActionType(ConnectionActionType.CONTROL_ACTION)).thenReturn(List.of(firstConnectionAction, secondConnectionAction));

        List<ControlActionReadDto> response = service.findAll();

        assertAll(
                () -> assertEquals(2, response.size()),
                () -> assertEquals(firstConnectionAction.getId(), response.getFirst().getId()),
                () -> assertEquals(firstConnectionAction.getName(), response.getFirst().getName()),
                () -> assertEquals(secondConnectionAction.getId(), response.getLast().getId()),
                () -> assertEquals(secondConnectionAction.getName(), response.getLast().getName())
        );
    }

    @Test
    @DisplayName("#findById > When no connection action is found > Throw an exception")
    void findByIdWhenNoConnectionActionIsFoundThrowAnException() {
        UUID connectionActionUUID = UUID.randomUUID();
        when(connectionActionRepository.findById(connectionActionUUID)).thenReturn(Optional.empty());

        assertThrows(Step2NotFoundException.class, () -> service.findById(connectionActionUUID));
    }

    @Test
    @DisplayName("#findById > When the connection action is found > Return it")
    void findByIdWhenTheConnectionActionIsFoundReturnIt() {
        ConnectionAction mockConnectionAction = assembleConnectionAction.get();

        when(connectionActionRepository.findById(mockConnectionAction.getId())).thenReturn(Optional.of(mockConnectionAction));

        ControlActionReadDto response = service.findById(mockConnectionAction.getId());
        assertEquals(mockConnectionAction.getId(), response.getId());
    }

    @Test
    @DisplayName("#findByComponentId > When no connections are found > Return an empty list")
    void findByComponentIdWhenNoConnectionsAreFoundReturnAnEmptyList() {
        UUID componentUUID = UUID.randomUUID();
        when(connectionRepository.findBySourceId(componentUUID)).thenReturn(List.of());
        assertTrue(service.findByComponentId(componentUUID).isEmpty());
    }

    @Test
    @DisplayName("#findByComponentId > When connection are found > Return a list of ControlActionReadDto ")
    void findByComponentIdWhenConnectionsAreFoundReturnAListOfControlActionReadDto() {
        UUID sourceUUID = UUID.randomUUID();
        Connection connection = assembleConnection.get();
        ConnectionAction firstConnectionAction = connection.getConnectionActions().getFirst();
        ConnectionAction SecondConnectionAction = connection.getConnectionActions().getLast();

        when(connectionRepository.findBySourceId(sourceUUID)).thenReturn(List.of(connection));


        List<ControlActionReadDto> response = service.findByComponentId(sourceUUID);

        assertAll(
                () -> assertEquals(2, response.size()),
                () -> assertEquals(firstConnectionAction.getId(), response.getFirst().getId()),
                () -> assertEquals(firstConnectionAction.getName(), response.getFirst().getName()),
                () -> assertEquals(SecondConnectionAction.getId(), response.getLast().getId()),
                () -> assertEquals(SecondConnectionAction.getName(), response.getFirst().getName())
        );
    }

    @Test
    @DisplayName("#findByConnectionId > When no connection action are found > Return an empty list")
    void findByConnectionIdWhenNoConnectionActionAreFoundReturnAnEmptyList() {
        UUID connectionUUID = UUID.randomUUID();
        when(connectionActionRepository.findByConnectionId(connectionUUID)).thenReturn(List.of());
        assertTrue(service.findByConnectionId(connectionUUID).isEmpty());
    }

    @Test
    @DisplayName("#findByConnectionId > When connection action are found > Return it")
    void findByConnectionIdWhenConnectionActionAreFoundReturnIt() {
        UUID connectionUUID = UUID.randomUUID();
        ConnectionAction firstConnectionAction = assembleConnectionAction.get();
        ConnectionAction SecondConnectionAction = assembleConnectionAction.get();

        when(connectionActionRepository.findByConnectionId(connectionUUID)).thenReturn(List.of(firstConnectionAction, SecondConnectionAction));

        List<ControlActionReadDto> response = service.findByConnectionId(connectionUUID);

        assertAll(
                () -> assertEquals(2, response.size()),
                () -> assertEquals(firstConnectionAction.getId(), response.getFirst().getId()),
                () -> assertEquals(firstConnectionAction.getCode(), response.getFirst().getCode()),
                () -> assertEquals(firstConnectionAction.getName(), response.getFirst().getName()),
                () -> assertEquals(SecondConnectionAction.getId(), response.getLast().getId()),
                () -> assertEquals(SecondConnectionAction.getCode(), response.getLast().getCode()),
                () -> assertEquals(SecondConnectionAction.getName(), response.getLast().getName())
        );
    }

    private final Supplier<ConnectionAction> assembleConnectionAction = () -> ConnectionAction.builder()
            .id(UUID.randomUUID())
            .code("Code")
            .name("Name")
            .connectionActionType(ConnectionActionType.CONTROL_ACTION)
            .connection(Connection.builder()
                    .id(UUID.randomUUID())
                    .source(new Controller())
                    .target(new Controller())
                    .build())
            .build();

    private final Supplier<Connection> assembleConnection = () -> Connection.builder()
            .id(UUID.randomUUID())
            .code("Code")
            .connectionActions(List.of(assembleConnectionAction.get(),assembleConnectionAction.get()))
            .build();
}
