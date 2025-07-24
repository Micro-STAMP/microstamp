package microstamp.step2.unit;

import microstamp.step2.dto.controlaction.ControlActionReadDto;
import microstamp.step2.entity.Connection;
import microstamp.step2.entity.Interaction;
import microstamp.step2.entity.Controller;
import microstamp.step2.enumeration.InteractionType;
import microstamp.step2.exception.Step2NotFoundException;
import microstamp.step2.repository.InteractionRepository;
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
    private InteractionRepository interactionRepository;

    @Test
    @DisplayName("#findAll > When no interaction are found > Return an empty list")
    void findAllWhenNoInteractionAreFoundReturnAnEmptyList() {
        when(interactionRepository.findByInteractionType(InteractionType.CONTROL_ACTION)).thenReturn(List.of());

        assertTrue(service.findAll().isEmpty());
    }

    @Test
    @DisplayName("#findAll > When the interactions are found > Return it")
    void findAllWhenTheInteractionsAreFoundReturnIt() {
        Interaction firstInteraction = assembleInteraction.get();
        Interaction secondInteraction = assembleInteraction.get();

        when(interactionRepository.findByInteractionType(InteractionType.CONTROL_ACTION)).thenReturn(List.of(firstInteraction, secondInteraction));

        List<ControlActionReadDto> response = service.findAll();

        assertAll(
                () -> assertEquals(2, response.size()),
                () -> assertEquals(firstInteraction.getId(), response.getFirst().getId()),
                () -> assertEquals(firstInteraction.getName(), response.getFirst().getName()),
                () -> assertEquals(secondInteraction.getId(), response.getLast().getId()),
                () -> assertEquals(secondInteraction.getName(), response.getLast().getName())
        );
    }

    @Test
    @DisplayName("#findById > When no interaction is found > Throw an exception")
    void findByIdWhenNoInteractionIsFoundThrowAnException() {
        UUID interactionUUID = UUID.randomUUID();
        when(interactionRepository.findById(interactionUUID)).thenReturn(Optional.empty());

        assertThrows(Step2NotFoundException.class, () -> service.findById(interactionUUID));
    }

    @Test
    @DisplayName("#findById > When the interaction is found > Return it")
    void findByIdWhenTheInteractionIsFoundReturnIt() {
        Interaction mockInteraction = assembleInteraction.get();

        when(interactionRepository.findById(mockInteraction.getId())).thenReturn(Optional.of(mockInteraction));

        ControlActionReadDto response = service.findById(mockInteraction.getId());
        assertEquals(mockInteraction.getId(), response.getId());
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
        Interaction firstInteraction = connection.getInteractions().getFirst();
        Interaction SecondInteraction = connection.getInteractions().getLast();

        when(connectionRepository.findBySourceId(sourceUUID)).thenReturn(List.of(connection));


        List<ControlActionReadDto> response = service.findByComponentId(sourceUUID);

        assertAll(
                () -> assertEquals(2, response.size()),
                () -> assertEquals(firstInteraction.getId(), response.getFirst().getId()),
                () -> assertEquals(firstInteraction.getName(), response.getFirst().getName()),
                () -> assertEquals(SecondInteraction.getId(), response.getLast().getId()),
                () -> assertEquals(SecondInteraction.getName(), response.getFirst().getName())
        );
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

        List<ControlActionReadDto> response = service.findByConnectionId(connectionUUID);

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

    private final Supplier<Interaction> assembleInteraction = () -> Interaction.builder()
            .id(UUID.randomUUID())
            .code("Code")
            .name("Name")
            .interactionType(InteractionType.CONTROL_ACTION)
            .connection(Connection.builder()
                    .id(UUID.randomUUID())
                    .source(new Controller())
                    .target(new Controller())
                    .build())
            .build();

    private final Supplier<Connection> assembleConnection = () -> Connection.builder()
            .id(UUID.randomUUID())
            .code("Code")
            .interactions(List.of(assembleInteraction.get(),assembleInteraction.get()))
            .build();
}