package microstamp.step2.unit;

import microstamp.step2.dto.interaction.InteractionInsertDto;
import microstamp.step2.dto.interaction.InteractionReadDto;
import microstamp.step2.dto.interaction.InteractionUpdateDto;
import microstamp.step2.entity.*;
import microstamp.step2.enumeration.InteractionType;
import microstamp.step2.exception.Step2InvalidInteractionException;
import microstamp.step2.exception.Step2NotFoundException;
import microstamp.step2.repository.InteractionRepository;
import microstamp.step2.repository.ConnectionRepository;
import microstamp.step2.service.impl.InteractionServiceImpl;
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
public class InteractionServiceUnitTest {

    @InjectMocks
    private InteractionServiceImpl service;

    @Mock
    private InteractionRepository interactionRepository;

    @Mock
    private ConnectionRepository connectionRepository;

    @Test
    @DisplayName("#findAll > When no interaction are found > Return an empty list")
    void findAllWhenNoInteractionAreFoundReturnAnEmptyList() {
        when(interactionRepository.findAll()).thenReturn(List.of());

        assertTrue(service.findAll().isEmpty());
    }

    @Test
    @DisplayName("#findAll > When the interactions are found > Return the interactions")
    void findAllWhenTheInteractionsAreFoundReturnTheInteractions() {
        Interaction firstInteraction = assembleInteraction.get();
        Interaction secondInteraction = assembleInteraction.get();

        when(interactionRepository.findAll()).thenReturn(List.of(firstInteraction, secondInteraction));

        List<InteractionReadDto> response = service.findAll();

        assertAll(
                () -> assertEquals(2, response.size()),
                () -> assertEquals(firstInteraction.getId(), response.getFirst().getId()),
                () -> assertEquals(secondInteraction.getId(), response.getLast().getId())
        );
    }

    @Test
    @DisplayName("#findById > When no interaction is found > Throw an exception")
    void findByIdWhenNoInteractionIsFoundThrowAnException() {
        UUID  interactionUUID = UUID.randomUUID();
        when(interactionRepository.findById(interactionUUID)).thenReturn(Optional.empty());

        assertThrows(Step2NotFoundException.class, () -> service.findById(interactionUUID));
    }

    @Test
    @DisplayName("#findById > When the interaction is found > Return it")
    void findByIdWhenTheInteractionIsFoundReturnIt() {
        Interaction mockInteraction = assembleInteraction.get();

        when(interactionRepository.findById(mockInteraction.getId())).thenReturn(Optional.of(mockInteraction));

        InteractionReadDto response = service.findById(mockInteraction.getId());
        assertEquals(mockInteraction.getId(), response.getId());
    }

    @Test
    @DisplayName("#findByConnectionId > When no interaction are found > Return an empty list")
    void findByConnectionIdWhenNoInteractionAreFoundReturnAnEmptyList() {
        UUID connectionUUID = UUID.randomUUID();
        when(interactionRepository.findByConnectionId(connectionUUID)).thenReturn(List.of());
        assertTrue(service.findByConnectionId(connectionUUID).isEmpty());
    }

    @Test
    @DisplayName("#findByConnectionId > When interaction are found > Return it")
    void findByConnectionIdWhenInteractionAreFoundReturnIt() {
        UUID connectionUUID = UUID.randomUUID();
        Interaction firstInteraction = assembleInteraction.get();
        Interaction SecondInteraction = assembleInteraction.get();

        when(interactionRepository.findByConnectionId(connectionUUID)).thenReturn(List.of(firstInteraction, SecondInteraction));

        List<InteractionReadDto> response = service.findByConnectionId(connectionUUID);

        assertAll(
                () -> assertEquals(2, response.size()),
                () -> assertEquals(firstInteraction.getId(), response.getFirst().getId()),
                () -> assertEquals(firstInteraction.getCode(), response.getFirst().getCode()),
                () -> assertEquals(firstInteraction.getName(), response.getFirst().getName()),
                () -> assertEquals(SecondInteraction.getId(), response.getLast().getId()),
                () -> assertEquals(SecondInteraction.getCode(), response.getLast().getCode()),
                () -> assertEquals(SecondInteraction.getName(), response.getLast().getName())
        );
    }

    @Test
    @DisplayName("#insert > When the associated connections is not found > Throw an exception ")
    void insertWhenTheAssociatedConnectionsIsNotFoundThrowAnException() {
        InteractionInsertDto mockInteractionInsert = assembleInteractionInsert.get();
        when(connectionRepository.findById(mockInteractionInsert.getConnectionId())).thenReturn(Optional.empty());

        assertThrows(Step2NotFoundException.class, () -> service.insert(mockInteractionInsert));
    }

    @Test
    @DisplayName("#insert > When the associated connections is found > Insert interaction ")
    void insertWhenTheAssociatedConnectionsIsFoundInsertInteraction() {
        InteractionInsertDto mockInteractionInsert = assembleInteractionInsert.get();
        when(connectionRepository.findById(mockInteractionInsert.getConnectionId())).thenReturn(Optional.of(assembleConnection.get()));

        InteractionReadDto response = service.insert(mockInteractionInsert);

        assertAll(
                () -> assertEquals(mockInteractionInsert.getName(), response.getName()),
                () -> assertEquals(mockInteractionInsert.getCode(), response.getCode()),
                () -> verify(interactionRepository, times(1)).save(any())
        );
    }

    @Test
    @DisplayName("#update > When the associated interaction is not found > Throw an exception ")
    void updateWhenTheAssociatedInteractionIsNotFoundThrowAnException() {
        UUID interactionUUID = UUID.randomUUID();
        when(interactionRepository.findById(interactionUUID)).thenReturn(Optional.empty());

        assertThrows(Step2NotFoundException.class, () -> service.update(interactionUUID,assembleInteractionUpdate.get()));
    }

    @Test
    @DisplayName("#update > When the associated interaction is found > Update the interaction ")
    void updateWhenTheAssociatedInteractionIsFoundUpdateTheInteraction() {
        Interaction mockInteraction = assembleInteraction.get();
        InteractionUpdateDto interactionUpdated = assembleInteractionUpdate.get();
        when(interactionRepository.findById(mockInteraction.getId())).thenReturn(Optional.of(mockInteraction));

        service.update(mockInteraction.getId(), interactionUpdated);

        mockInteraction.setName(interactionUpdated.getName());
        mockInteraction.setCode(interactionUpdated.getCode());
        mockInteraction.setInteractionType(interactionUpdated.getInteractionType());

        verify(interactionRepository, times(1)).save(mockInteraction);
    }

    @Test
    @DisplayName("#delete > When the interaction is not found > Throw an exception")
    void deleteWhenTheInteractionIsNotFoundThrowAnException() {
        UUID interactionUUID = UUID.randomUUID();
        when(interactionRepository.findById(interactionUUID)).thenReturn(Optional.empty());

        assertThrows(Step2NotFoundException.class, () -> service.delete(interactionUUID));
    }

    @Test
    @DisplayName("#delete > When the interaction is found > Delete it")
    void deleteWhenTheInteractionIsFoundDeleteIt() {
        Interaction mockInteraction = assembleInteraction.get();

        when(interactionRepository.findById(mockInteraction.getId())).thenReturn(Optional.of(mockInteraction));

        service.delete(mockInteraction.getId());
        verify(interactionRepository, times(1)).deleteById(mockInteraction.getId());
    }

    @Test
    @DisplayName("#validate > When the connection source is an environment and the connection type is not process input or disturbance > Throw an exception")
    void validateWhenTheConnectionSourceIsAnEnvironmentAndTheConnectionTypeIsNotProcessInputOrDisturbanceThrowAnException() {
        assertThrows(Step2InvalidInteractionException.class, () ->  service.validate(assembleConnectionForValidate.get(),InteractionType.FEEDBACK));
    }

    @Test
    @DisplayName("#validate > When the connection target is an environment and the connection type is not a process output > Throw an exception")
    void validateWhenTheConnectionTargetIsAnEnvironmentAndTheConnectionTypeIsNotProcessOutputThrowAnException() {
        assertThrows(Step2InvalidInteractionException.class, () ->  service.validate(assembleConnectionForValidate.get(),InteractionType.PROCESS_INPUT));
    }

    @Test
    @DisplayName("#validate > When the connection target and source is not an environment and the connection type is not control action or feedback or communication channel > Throw an exception")
    void validateWhenTheConnectionTargetAndSourceIsNotAnEnvironmentAndTheConnectionTypeIsNotControlActionOrFeedbackOrCommunicationChannelThrowAnException() {
        assertThrows(Step2InvalidInteractionException.class, () ->  service.validate(assembleConnection.get(),InteractionType.PROCESS_INPUT));
    }

    @Test
    @DisplayName("#validate > When the connection target and source is valid and the connection type is valid > Continue")
    void validateWhenTheConnectionTargetAndSourceIsValidAndTheConnectionTypeIsValidContinue() {
        assertDoesNotThrow(() -> service.validate(assembleConnection.get(),InteractionType.CONTROL_ACTION));
    }

    private final Supplier<Interaction> assembleInteraction = () -> Interaction.builder()
            .id(UUID.randomUUID())
            .code("Code")
            .name("Name")
            .connection(new Connection())
            .build();

    private final Supplier<InteractionInsertDto> assembleInteractionInsert = () -> InteractionInsertDto.builder()
            .code("Code")
            .name("Name")
            .connectionId(UUID.randomUUID())
            .interactionType(InteractionType.CONTROL_ACTION)
            .build();

    private final Supplier<Connection> assembleConnection = () -> Connection.builder()
            .id(UUID.randomUUID())
            .code("Code")
            .source(new Controller())
            .target(new ControlledProcess())
            .build();

    private final Supplier<InteractionUpdateDto> assembleInteractionUpdate = () -> InteractionUpdateDto.builder()
            .name("Updated name")
            .code("Updated code")
            .interactionType(InteractionType.CONTROL_ACTION)
            .build();

    private final Supplier<Connection> assembleConnectionForValidate = () -> Connection.builder()
            .id(UUID.randomUUID())
            .code("Code")
            .source(Environment.builder().build())
            .target(Environment.builder().build())
            .build();
}