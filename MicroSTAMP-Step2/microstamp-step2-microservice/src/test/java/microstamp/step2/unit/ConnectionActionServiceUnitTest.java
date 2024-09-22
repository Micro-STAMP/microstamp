package microstamp.step2.unit;

import microstamp.step2.dto.connectionaction.ConnectionActionInsertDto;
import microstamp.step2.dto.connectionaction.ConnectionActionReadDto;
import microstamp.step2.dto.connectionaction.ConnectionActionUpdateDto;
import microstamp.step2.dto.controlaction.ControlActionReadDto;
import microstamp.step2.dto.state.StateInsertDto;
import microstamp.step2.dto.state.StateReadDto;
import microstamp.step2.dto.state.StateUpdateDto;
import microstamp.step2.entity.*;
import microstamp.step2.enumeration.ConnectionActionType;
import microstamp.step2.exception.Step2InvalidConnectionActionException;
import microstamp.step2.exception.Step2NotFoundException;
import microstamp.step2.repository.ConnectionActionRepository;
import microstamp.step2.repository.ConnectionRepository;
import microstamp.step2.service.impl.ConnectionActionServiceImpl;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConnectionActionServiceUnitTest {

    @InjectMocks
    private ConnectionActionServiceImpl service;

    @Mock
    private ConnectionActionRepository connectionActionRepository;

    @Mock
    private ConnectionRepository connectionRepository;

    @Test
    @DisplayName("#findAll > When no connection action are found > Return an empty list")
    void findAllWhenNoConnectionActionAreFoundReturnAnEmptyList() {
        when(connectionActionRepository.findAll()).thenReturn(List.of());

        assertTrue(service.findAll().isEmpty());
    }

    @Test
    @DisplayName("#findAll > When the connection action are found > Return the connection actions")
    void findAllWhenTheConnectionActionsAreFoundReturnTheConnectionActions() {
        ConnectionAction firstConnectionAction = assembleConnectionAction.get();
        ConnectionAction secondConnectionAction = assembleConnectionAction.get();

        when(connectionActionRepository.findAll()).thenReturn(List.of(firstConnectionAction, secondConnectionAction));

        List<ConnectionActionReadDto> response = service.findAll();

        assertAll(
                () -> assertEquals(2, response.size()),
                () -> assertEquals(firstConnectionAction.getId(), response.getFirst().getId()),
                () -> assertEquals(secondConnectionAction.getId(), response.getLast().getId())
        );
    }

    @Test
    @DisplayName("#findById > When no connection action is found > Throw an exception")
    void findByIdWhenNoConnectionActionIsFoundThrowAnException() {
        UUID  connectionActionUUID = UUID.randomUUID();
        when(connectionActionRepository.findById(connectionActionUUID)).thenReturn(Optional.empty());

        assertThrows(Step2NotFoundException.class, () -> service.findById(connectionActionUUID));
    }

    @Test
    @DisplayName("#findById > When the connection action is found > Return it")
    void findByIdWhenTheConnectionActionIsFoundReturnIt() {
        ConnectionAction mockConnectionAction = assembleConnectionAction.get();

        when(connectionActionRepository.findById(mockConnectionAction.getId())).thenReturn(Optional.of(mockConnectionAction));

        ConnectionActionReadDto response = service.findById(mockConnectionAction.getId());
        assertEquals(mockConnectionAction.getId(), response.getId());
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

        List<ConnectionActionReadDto> response = service.findByConnectionId(connectionUUID);

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

    @Test
    @DisplayName("#insert > When the associated connections is not found > Throw an exception ")
    void insertWhenTheAssociatedConnectionsIsNotFoundThrowAnException() {
        ConnectionActionInsertDto mockConnectionActionInsert = assembleConnectionActionInsert.get();
        when(connectionRepository.findById(mockConnectionActionInsert.getConnectionId())).thenReturn(Optional.empty());

        assertThrows(Step2NotFoundException.class, () -> service.insert(mockConnectionActionInsert));
    }

    @Test
    @DisplayName("#insert > When the associated connections is found > Insert connection action ")
    void insertWhenTheAssociatedConnectionsIsFoundInsertConnectionAction() {
        ConnectionActionInsertDto mockConnectionActionInsert = assembleConnectionActionInsert.get();
        when(connectionRepository.findById(mockConnectionActionInsert.getConnectionId())).thenReturn(Optional.of(assembleConnection.get()));

        ConnectionActionReadDto response = service.insert(mockConnectionActionInsert);

        assertAll(
                () -> assertEquals(mockConnectionActionInsert.getName(), response.getName()),
                () -> assertEquals(mockConnectionActionInsert.getCode(), response.getCode()),
                () -> verify(connectionActionRepository, times(1)).save(any())
        );
    }

    @Test
    @DisplayName("#update > When the associated connection action is not found > Throw an exception ")
    void updateWhenTheAssociatedConnectionActionIsNotFoundThrowAnException() {
        UUID connectionActionUUID = UUID.randomUUID();
        when(connectionActionRepository.findById(connectionActionUUID)).thenReturn(Optional.empty());

        assertThrows(Step2NotFoundException.class, () -> service.update(connectionActionUUID,assembleConnectionActionUpdate.get()));
    }

    @Test
    @DisplayName("#update > When the associated connection action is found > Update the connection action ")
    void updateWhenTheAssociatedConnectionActionIsFoundUpdateTheConnectionAction() {
        ConnectionAction mockConnectionAction = assembleConnectionAction.get();
        ConnectionActionUpdateDto connectionActionUpdated = assembleConnectionActionUpdate.get();
        when(connectionActionRepository.findById(mockConnectionAction.getId())).thenReturn(Optional.of(mockConnectionAction));

        service.update(mockConnectionAction.getId(), connectionActionUpdated);

        mockConnectionAction.setName(connectionActionUpdated.getName());
        mockConnectionAction.setCode(connectionActionUpdated.getCode());
        mockConnectionAction.setConnectionActionType(connectionActionUpdated.getConnectionActionType());

        verify(connectionActionRepository, times(1)).save(mockConnectionAction);
    }

    @Test
    @DisplayName("#delete > When the connection action is not found > Throw an exception")
    void deleteWhenTheConnectionActionIsNotFoundThrowAnException() {
        UUID connectionActionUUID = UUID.randomUUID();
        when(connectionActionRepository.findById(connectionActionUUID)).thenReturn(Optional.empty());

        assertThrows(Step2NotFoundException.class, () -> service.delete(connectionActionUUID));
    }

    @Test
    @DisplayName("#delete > When the connection action is found > Delete it")
    void deleteWhenTheConnectionActionIsFoundDeleteIt() {
        ConnectionAction mockConnectionAction = assembleConnectionAction.get();

        when(connectionActionRepository.findById(mockConnectionAction.getId())).thenReturn(Optional.of(mockConnectionAction));

        service.delete(mockConnectionAction.getId());
        verify(connectionActionRepository, times(1)).deleteById(mockConnectionAction.getId());
    }

    @Test
    @DisplayName("#validate > When the connection source is an environment and the connection type is not process input or disturbance > Throw an exception")
    void validateWhenTheConnectionSourceIsAnEnvironmentAndTheConnectionTypeIsNotProcessInputOrDisturbanceThrowAnException() {
        assertThrows(Step2InvalidConnectionActionException.class, () ->  service.validate(assembleConnectionForValidate.get(),ConnectionActionType.FEEDBACK));
    }

    @Test
    @DisplayName("#validate > When the connection target is an environment and the connection type is not a process output > Throw an exception")
    void validateWhenTheConnectionTargetIsAnEnvironmentAndTheConnectionTypeIsNotProcessOutputThrowAnException() {
        assertThrows(Step2InvalidConnectionActionException.class, () ->  service.validate(assembleConnectionForValidate.get(),ConnectionActionType.PROCESS_INPUT));
    }

    @Test
    @DisplayName("#validate > When the connection target and source is not an environment and the connection type is not control action or feedback or communication channel > Throw an exception")
    void validateWhenTheConnectionTargetAndSourceIsNotAnEnvironmentAndTheConnectionTypeIsNotControlActionOrFeedbackOrCommunicationChannelThrowAnException() {
        assertThrows(Step2InvalidConnectionActionException.class, () ->  service.validate(assembleConnection.get(),ConnectionActionType.PROCESS_INPUT));
    }

    @Test
    @DisplayName("#validate > When the connection target and source is valid and the connection type is valid > Continue")
    void validateWhenTheConnectionTargetAndSourceIsValidAndTheConnectionTypeIsValidContinue() {
        assertDoesNotThrow(() -> service.validate(assembleConnection.get(),ConnectionActionType.CONTROL_ACTION));
    }

    private final Supplier<ConnectionAction> assembleConnectionAction = () -> ConnectionAction.builder()
            .id(UUID.randomUUID())
            .code("Code")
            .name("Name")
            .connection(new Connection())
            .build();

    private final Supplier<ConnectionActionInsertDto> assembleConnectionActionInsert = () -> ConnectionActionInsertDto.builder()
            .code("Code")
            .name("Name")
            .connectionId(UUID.randomUUID())
            .connectionActionType(ConnectionActionType.CONTROL_ACTION)
            .build();

    private final Supplier<Connection> assembleConnection = () -> Connection.builder()
            .id(UUID.randomUUID())
            .code("Code")
            .source(new Controller())
            .target(new ControlledProcess())
            .build();

    private final Supplier<ConnectionActionUpdateDto> assembleConnectionActionUpdate = () -> ConnectionActionUpdateDto.builder()
            .name("Updated name")
            .code("Updated code")
            .connectionActionType(ConnectionActionType.CONTROL_ACTION)
            .build();

    private final Supplier<Connection> assembleConnectionForValidate = () -> Connection.builder()
            .id(UUID.randomUUID())
            .code("Code")
            .source(Environment.builder().build())
            .target(Environment.builder().build())
            .build();
}
