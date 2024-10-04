package step3.unit;

import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import step3.dto.auth.AnalysisReadDto;
import step3.dto.mapper.UnsafeControlActionMapper;
import step3.dto.step1.HazardReadDto;
import step3.dto.step2.ControlActionReadDto;
import step3.dto.step2.StateReadDto;
import step3.dto.unsafe_control_action.UnsafeControlActionCreateDto;
import step3.dto.unsafe_control_action.UnsafeControlActionReadDto;
import step3.entity.Rule;
import step3.entity.UCAType;
import step3.entity.UnsafeControlAction;
import step3.entity.association.RuleState;
import step3.infra.exceptions.OperationNotAllowedException;
import step3.proxy.AuthServerProxy;
import step3.proxy.Step1Proxy;
import step3.proxy.Step2Proxy;
import step3.repository.RuleRepository;
import step3.repository.StateAssociationRepository;
import step3.repository.UnsafeControlActionRepository;
import step3.service.UnsafeControlActionService;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnsafeControlActionServiceUnitTest {

    @InjectMocks
    private UnsafeControlActionService service;

    @Mock
    private Step1Proxy step1Proxy;

    @Mock
    private Step2Proxy step2Proxy;

    @Mock
    private RuleRepository ruleRepository;

    @Mock
    private AuthServerProxy authServerProxy;

    @Mock
    private UnsafeControlActionMapper mapper;

    @Mock
    private StateAssociationRepository stateAssociationRepository;

    @Mock
    private UnsafeControlActionRepository unsafeControlActionRepository;

    @Test
    @DisplayName("#createUnsafeControlAction > When there is an error when communicating with auth server proxy > Throw an exception")
    void createUnsafeControlActionWhenThereIsAnErrorWhenCommunicatingWithAuthServerProxyThrowAnException() {
        UnsafeControlActionCreateDto mock = UnsafeControlActionCreateDto.builder()
                .analysis_id(UUID.randomUUID())
                .build();

        when(authServerProxy.getAnalysisById(mock.analysis_id())).thenThrow(FeignException.class);

        assertThrows(FeignException.class, () -> service.createUnsafeControlAction(mock));
    }


    @Test
    @DisplayName("#createUnsafeControlAction > When there is an error when communicating with Step 2 proxy > Throw an exception")
    void createUnsafeControlActionWhenThereIsAnErrorWhenCommunicatingWithStep2ProxyThrowAnException() {
        UnsafeControlActionCreateDto mock = UnsafeControlActionCreateDto.builder()
                .analysis_id(UUID.randomUUID())
                .control_action_id(UUID.randomUUID())
                .build();

        when(authServerProxy.getAnalysisById(mock.analysis_id())).thenReturn(AnalysisReadDto.builder().build());
        when(step2Proxy.getControlActionById(mock.control_action_id())).thenThrow(FeignException.class);

        assertAll(
                () -> assertThrows(FeignException.class, () -> service.createUnsafeControlAction(mock)),
                () -> verify(authServerProxy, times(1)).getAnalysisById(mock.analysis_id()),
                () -> verify(step2Proxy, times(1)).getControlActionById(mock.control_action_id())
        );
    }

    @Test
    @DisplayName("#createUnsafeControlAction > When there is an error when communicating with Step 1 proxy > Throw an exception")
    void createUnsafeControlActionWhenThereIsAnErrorWhenCommunicatingWithStep1ProxyThrowAnException() {
        UnsafeControlActionCreateDto mock = UnsafeControlActionCreateDto.builder()
                .analysis_id(UUID.randomUUID())
                .control_action_id(UUID.randomUUID())
                .states_ids(List.of(UUID.randomUUID(), UUID.randomUUID()))
                .hazard_id(UUID.randomUUID())
                .build();

        when(authServerProxy.getAnalysisById(mock.analysis_id())).thenReturn(AnalysisReadDto.builder().build());
        when(step1Proxy.getHazardById(mock.hazard_id())).thenThrow(FeignException.class);

        assertAll(
                () -> assertThrows(FeignException.class, () -> service.createUnsafeControlAction(mock)),
                () -> verify(authServerProxy, times(1)).getAnalysisById(mock.analysis_id()),
                () -> verify(step1Proxy, times(1)).getHazardById(mock.hazard_id())
        );
    }

    @Test
    @DisplayName("#createUnsafeControlAction > When the provided parameters are correct > When there is no error when communicating with other microservices > Create the unsafe control action")
    void createUnsafeControlActionWhenTheProvidedParametersAreCorrectWhenThereIsNoErrorWhenCommunicatingWithOtherMicroservicesCreateTheUnsafeControlAction() {
        UnsafeControlActionCreateDto mock = UnsafeControlActionCreateDto.builder()
                .analysis_id(UUID.randomUUID())
                .control_action_id(UUID.randomUUID())
                .states_ids(List.of(UUID.randomUUID(), UUID.randomUUID()))
                .hazard_id(UUID.randomUUID())
                .rule_code("-")
                .build();

        when(authServerProxy.getAnalysisById(mock.analysis_id())).thenReturn(AnalysisReadDto.builder().build());
        when(step2Proxy.getControlActionById(mock.control_action_id())).thenReturn(ControlActionReadDto.builder().id(mock.control_action_id()).build());
        when(step1Proxy.getHazardById(mock.hazard_id())).thenReturn(HazardReadDto.builder().code("H-1").id(mock.hazard_id()).build());
        when(unsafeControlActionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(mapper.toUcaReadDto(any())).thenAnswer(invocation -> {
            UnsafeControlAction uca = invocation.getArgument(0);
            return UnsafeControlActionReadDto.builder()
                    .id(uca.getId())
                    .hazard_code("H-1")
                    .analysis_id(uca.getAnalysisId())
                    .build();
        });

        UnsafeControlActionReadDto response = service.createUnsafeControlAction(mock);

        assertAll(
                () -> verify(authServerProxy, times(1)).getAnalysisById(mock.analysis_id()),
                () -> verify(step2Proxy, times(1)).getControlActionById(mock.control_action_id()),
                () -> verify(step1Proxy, times(1)).getHazardById(mock.hazard_id()),
                () -> verify(unsafeControlActionRepository, times(2)).save(any()),
                () -> assertEquals("H-1", response.hazard_code())
        );
    }

    @Test
    @DisplayName("#createUCAsByRule > When no rule is found > Throw an exception")
    void createUcAsByRuleWhenNoRuleIsFoundThrowAnException() {
        UUID mockRuleUUID = UUID.randomUUID();

        when(ruleRepository.findById(mockRuleUUID)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> service.createUCAsByRule(mockRuleUUID));
    }

    @Test
    @DisplayName("#createUCAsByRule > When the rule is found > Create the UCAs")
    void createUcAsByRuleWhenTheRuleIsFoundCreateTheUcAs() {
        UUID mockRuleUUID = UUID.randomUUID();
        Rule mock = Rule.builder()
                .analysisId(UUID.randomUUID())
                .controlActionId(UUID.randomUUID())
                .stateAssociations(List.of(
                        RuleState.builder().stateId(UUID.randomUUID()).build(),
                        RuleState.builder().stateId(UUID.randomUUID()).build()))
                .hazardId(UUID.randomUUID())
                .types(new HashSet<>() {{ add(UCAType.PROVIDED); }})
                .build();

        when(ruleRepository.findById(mockRuleUUID)).thenReturn(Optional.of(mock));
        when(authServerProxy.getAnalysisById(mock.getAnalysisId())).thenReturn(AnalysisReadDto.builder().build());
        when(step2Proxy.getControlActionById(mock.getControlActionId())).thenReturn(ControlActionReadDto.builder().id(mock.getControlActionId()).build());
        when(step1Proxy.getHazardById(mock.getHazardId())).thenReturn(HazardReadDto.builder().code("H-1").id(mock.getHazardId()).build());
        when(unsafeControlActionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(mapper.toUcaReadDto(any())).thenAnswer(invocation -> {
            UnsafeControlAction uca = invocation.getArgument(0);
            return UnsafeControlActionReadDto.builder()
                    .id(uca.getId())
                    .hazard_code("H-1")
                    .analysis_id(uca.getAnalysisId())
                    .build();
        });

        List<UnsafeControlActionReadDto> response = service.createUCAsByRule(mockRuleUUID);

        assertAll(
                () -> verify(authServerProxy, times(1)).getAnalysisById(mock.getAnalysisId()),
                () -> verify(step2Proxy, times(1)).getControlActionById(mock.getControlActionId()),
                () -> verify(step1Proxy, times(1)).getHazardById(mock.getHazardId()),
                () -> verify(unsafeControlActionRepository, times(2)).save(any()),
                () -> assertEquals("H-1", response.getFirst().hazard_code())
        );
    }


    @Test
    @DisplayName("#readUnsafeControlAction > When no unsafe control action is found > Throw an exception")
    void readUnsafeControlActionWhenNoUnsafeControlActionIsFoundThrowAnException() {
        UUID mockUnsafeControlActionId = UUID.randomUUID();

        when(unsafeControlActionRepository.findById(mockUnsafeControlActionId)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> service.readUnsafeControlAction(mockUnsafeControlActionId));
    }

    @Test
    @DisplayName("#readUnsafeControlAction > When the unsafe control action is found > Return the unsafe control action")
    void readUnsafeControlActionWhenTheUnsafeControlActionIsFoundReturnTheUnsafeControlAction() {
        UnsafeControlAction mockUnsafeControlAction = assembleUnsafeControlAction.get();
        UnsafeControlActionReadDto mockUnsafeControlActionRead = assembleUnsafeControlActionRead.apply(mockUnsafeControlAction.getId());

        when(unsafeControlActionRepository.findById(mockUnsafeControlAction.getId())).thenReturn(Optional.of(mockUnsafeControlAction));
        when(mapper.toUcaReadDto(mockUnsafeControlAction)).thenReturn(mockUnsafeControlActionRead);

        UnsafeControlActionReadDto response = service.readUnsafeControlAction(mockUnsafeControlActionRead.id());

        assertEquals(mockUnsafeControlActionRead, response);
    }


    @Test
    @DisplayName("#readAllUnsafeControlActions > When no unsafe control action is found > Return an empty list")
    void readAllUnsafeControlActionsWhenNoUnsafeControlActionIsFoundReturnAnEmptyList() {

        when(unsafeControlActionRepository.findAll()).thenReturn(List.of());

        assertTrue(service.readAllUnsafeControlActions().isEmpty());
    }

    @Test
    @DisplayName("#readAllUnsafeControlActions > When the unsafe control action are found > Return the list of unsafe control actions")
    void readAllUnsafeControlActionsWhenTheUnsafeControlActionAreFoundReturnTheListOfUnsafeControlActions() {
        UnsafeControlAction mockFirst = assembleUnsafeControlAction.get();
        UnsafeControlAction mockSecond = assembleUnsafeControlAction.get();
        UnsafeControlActionReadDto mockFirstRead = assembleUnsafeControlActionRead.apply(mockFirst.getId());
        UnsafeControlActionReadDto mockSecondRead = assembleUnsafeControlActionRead.apply(mockSecond.getId());

        when(unsafeControlActionRepository.findAll()).thenReturn(List.of(mockFirst, mockSecond));
        when(mapper.toUcaReadDtoList(List.of(mockFirst, mockSecond))).thenReturn(List.of(mockFirstRead, mockSecondRead));

        List<UnsafeControlActionReadDto> result = service.readAllUnsafeControlActions();

        assertAll(
                () -> assertEquals(2, result.size()),
                () -> assertEquals(mockFirstRead, result.getFirst()),
                () -> assertEquals(mockSecondRead, result.getLast())
        );
    }

    @Test
    @DisplayName("#readAllUCAByControlActionId > When no unsafe control action is found > Return an empty list")
    void readAllUCAByControlActionIdWhenNoUnsafeControlActionIsFoundReturnAnEmptyList() {
        UUID mockControlActionId = UUID.randomUUID();

        when(unsafeControlActionRepository.findByControlActionId(mockControlActionId)).thenReturn(List.of());

        assertTrue(service.readAllUCAByControlActionId(mockControlActionId).isEmpty());
    }

    @Test
    @DisplayName("#readAllUCAByControlActionId > When the unsafe control action are found > Return the list of unsafe control actions")
    void readAllUCAByControlActionIdWhenTheUnsafeControlActionAreFoundReturnTheListOfUnsafeControlActions() {
        UUID mockControlActionId = UUID.randomUUID();
        UnsafeControlAction mockFirst = assembleUnsafeControlAction.get();
        UnsafeControlAction mockSecond = assembleUnsafeControlAction.get();
        UnsafeControlActionReadDto mockFirstRead = assembleUnsafeControlActionRead.apply(mockFirst.getId());
        UnsafeControlActionReadDto mockSecondRead = assembleUnsafeControlActionRead.apply(mockSecond.getId());

        when(unsafeControlActionRepository.findByControlActionId(mockControlActionId)).thenReturn(List.of(mockFirst, mockSecond));
        when(mapper.toUcaReadDtoList(List.of(mockFirst, mockSecond))).thenReturn(List.of(mockFirstRead, mockSecondRead));

        List<UnsafeControlActionReadDto> result = service.readAllUCAByControlActionId(mockControlActionId);

        assertAll(
                () -> assertEquals(2, result.size()),
                () -> assertEquals(mockFirstRead, result.getFirst()),
                () -> assertEquals(mockSecondRead, result.getLast())
        );
    }

    @Test
    @DisplayName("#readAllUCAByAnalysisId > When no unsafe control action is found > Return an empty list")
    void readAllUcaByAnalysisIdWhenNoUnsafeControlActionIsFoundReturnAnEmptyList() {
        UUID mockAnalysisId = UUID.randomUUID();

        when(unsafeControlActionRepository.findByAnalysisId(mockAnalysisId)).thenReturn(List.of());

        assertTrue(service.readAllUCAByAnalysisId(mockAnalysisId).isEmpty());
    }

    @Test
    @DisplayName("#readAllUCAByAnalysisId > When the unsafe control action are found > Return the list of unsafe control actions")
    void readAllUcaByAnalysisIdWhenTheUnsafeControlActionAreFoundReturnTheListOfUnsafeControlActions() {
        UUID mockAnalysisId = UUID.randomUUID();
        UnsafeControlAction mockFirst = assembleUnsafeControlAction.get();
        UnsafeControlAction mockSecond = assembleUnsafeControlAction.get();
        UnsafeControlActionReadDto mockFirstRead = assembleUnsafeControlActionRead.apply(mockFirst.getId());
        UnsafeControlActionReadDto mockSecondRead = assembleUnsafeControlActionRead.apply(mockSecond.getId());

        when(unsafeControlActionRepository.findByAnalysisId(mockAnalysisId)).thenReturn(List.of(mockFirst, mockSecond));
        when(mapper.toUcaReadDtoList(List.of(mockFirst, mockSecond))).thenReturn(List.of(mockFirstRead, mockSecondRead));

        List<UnsafeControlActionReadDto> result = service.readAllUCAByAnalysisId(mockAnalysisId);

        assertAll(
                () -> assertEquals(2, result.size()),
                () -> assertEquals(mockFirstRead, result.getFirst()),
                () -> assertEquals(mockSecondRead, result.getLast())
        );
    }

    @Test
    @DisplayName("#deleteUnsafeControlAction > When no unsafe control action is found > Throw an exception")
    void deleteUnsafeControlActionWhenNoUnsafeControlActionIsFoundThrowAnException() {
        UUID mockUnsafeControlActionId = UUID.randomUUID();

        when(unsafeControlActionRepository.findById(mockUnsafeControlActionId)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> service.deleteUnsafeControlAction(mockUnsafeControlActionId));
    }

    @Test
    @DisplayName("#deleteUnsafeControlAction > When the unsafe control action is found > When no rule code is found > Throw an exception")
    void deleteUnsafeControlActionWhenTheUnsafeControlActionIsFoundWhenNoRuleCodeIsFoundThrowAnException() {
        UnsafeControlAction mockUnsafeControlAction = assembleUnsafeControlAction.get();
        mockUnsafeControlAction.setRuleCode("Rule Code");

        when(unsafeControlActionRepository.findById(mockUnsafeControlAction.getId())).thenReturn(Optional.of(mockUnsafeControlAction));

        assertThrows(OperationNotAllowedException.class, () -> service.deleteUnsafeControlAction(mockUnsafeControlAction.getId()));
    }

    @Test
    @DisplayName("#deleteUnsafeControlAction > When the unsafe control action is found > Delete the states associated and the unsafe control action")
    void deleteUnsafeControlActionWhenTheUnsafeControlActionIsFoundDeleteTheStatesAssociatedAndTheUnsafeControlAction() {
        UnsafeControlAction mockUnsafeControlAction = assembleUnsafeControlAction.get();

        when(unsafeControlActionRepository.findById(mockUnsafeControlAction.getId())).thenReturn(Optional.of(mockUnsafeControlAction));

        service.deleteUnsafeControlAction(mockUnsafeControlAction.getId());

        assertAll(
                () -> verify(stateAssociationRepository, times(1)).deleteAllByUnsafeControlActionId(mockUnsafeControlAction.getId()),
                () -> verify(unsafeControlActionRepository, times(1)).deleteById(mockUnsafeControlAction.getId())
        );
    }

    @Test
    @DisplayName("#getUCAStates > When the list of states are empty > Return an empty list")
    void getUcaStatesWhenTheListOfStatesAreEmptyReturnAnEmptyList() {
        List<UUID> mockStateIds = List.of();

        List<StateReadDto> response = service.getUCAStates(mockStateIds);
        assertAll(
                () -> assertTrue(response.isEmpty()),
                () -> verify(step2Proxy, times(0)).getStateById(any())
        );
    }

    @Test
    @DisplayName("#getUCAStates > When the list of states are not empty > Return a list with the fetched states")
    void getUcaStatesWhenTheListOfStatesAreNotEmptyReturnAListWithTheFetchedStates() {
        List<UUID> mockStateIds = List.of(UUID.randomUUID(), UUID.randomUUID());

        when(step2Proxy.getStateById(mockStateIds.getFirst())).thenReturn(assembleStateReadDTO.apply(mockStateIds.getFirst()));
        when(step2Proxy.getStateById(mockStateIds.getLast())).thenReturn(assembleStateReadDTO.apply(mockStateIds.getLast()));

        List<StateReadDto> response = service.getUCAStates(mockStateIds);
        assertAll(
                () -> assertEquals(2, response.size()),
                () -> verify(step2Proxy, times(1)).getStateById(mockStateIds.getFirst()),
                () -> verify(step2Proxy, times(1)).getStateById(mockStateIds.getLast())
        );
    }

    private final Supplier<UnsafeControlAction> assembleUnsafeControlAction = () -> UnsafeControlAction.builder()
            .id(UUID.randomUUID())
            .hazardId(UUID.randomUUID())
            .type(UCAType.PROVIDED)
            .stateAssociations(new ArrayList<>())
            .build();

    private final Function<UUID, UnsafeControlActionReadDto> assembleUnsafeControlActionRead = (id) -> UnsafeControlActionReadDto.builder()
            .id(id)
            .name("UCA")
            .build();

    private final Function<UUID, StateReadDto> assembleStateReadDTO = (uuid) -> StateReadDto.builder()
            .id(UUID.randomUUID())
            .code("S1")
            .name("State Name")
            .build();

}
