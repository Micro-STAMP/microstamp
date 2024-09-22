package step3.unit;

import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import step3.dto.mapper.RuleMapper;
import step3.dto.rule.RuleCreateDto;
import step3.dto.rule.RuleReadDto;
import step3.dto.rule.RuleReadListDto;
import step3.entity.Rule;
import step3.entity.UCAType;
import step3.entity.UnsafeControlAction;
import step3.proxy.AuthServerProxy;
import step3.proxy.Step1Proxy;
import step3.proxy.Step2Proxy;
import step3.repository.RuleRepository;
import step3.repository.UnsafeControlActionRepository;
import step3.service.RuleService;

import java.util.*;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RuleServiceUnitTest {

    @InjectMocks
    private RuleService service;

    @Mock
    private RuleRepository ruleRepository;

    @Mock
    private UnsafeControlActionRepository ucaRepository;

    @Mock
    private Step1Proxy step1Proxy;

    @Mock
    private Step2Proxy step2Proxy;

    @Mock
    private AuthServerProxy authServerProxy;

    @Mock
    private RuleMapper mapper;

    @Test
    @DisplayName("#createRule > When there is an error when communicating with auth server proxy > Throw an exception")
    void createRuleWhenThereIsAnErrorWhenCommunicatingWithAuthServerProxyThrowAnException() {
        RuleCreateDto mock = assembleRuleCreate.get();

        when(authServerProxy.getAnalysisById(mock.analysis_id())).thenThrow(FeignException.class);

        assertAll(
                () -> assertThrows(FeignException.class, () -> service.createRule(mock)),
                () -> verify(authServerProxy, times(1)).getAnalysisById(mock.analysis_id()),
                () -> verify(step2Proxy, times(0)).getControlActionById(mock.control_action_id()),
                () -> verify(step1Proxy, times(0)).getHazardById(mock.hazard_id())
        );
    }

    @Test
    @DisplayName("#createRule > When there is an error when communicating with Step 2 proxy > Throw an exception")
    void createRuleWhenThereIsAnErrorWhenCommunicatingWithStep2ProxyThrowAnException() {
        RuleCreateDto mock = assembleRuleCreate.get();

        when(authServerProxy.getAnalysisById(mock.analysis_id())).thenReturn(null);
        when(step2Proxy.getControlActionById(mock.control_action_id())).thenThrow(FeignException.class);

        assertAll(
                () -> assertThrows(FeignException.class, () -> service.createRule(mock)),
                () -> verify(authServerProxy, times(1)).getAnalysisById(mock.analysis_id()),
                () -> verify(step2Proxy, times(1)).getControlActionById(mock.control_action_id()),
                () -> verify(step1Proxy, times(0)).getHazardById(mock.hazard_id())
        );
    }

    @Test
    @DisplayName("#createRule > When there is an error when communicating with Step 1 proxy > Throw an exception")
    void createRuleWhenThereIsAnErrorWhenCommunicatingWithStep1ProxyThrowAnException() {
        RuleCreateDto mock = assembleRuleCreate.get();

        when(authServerProxy.getAnalysisById(mock.analysis_id())).thenReturn(null);
        when(step2Proxy.getControlActionById(mock.control_action_id())).thenReturn(null);
        when(step1Proxy.getHazardById(mock.hazard_id())).thenThrow(FeignException.class);

        assertAll(
                () -> assertThrows(FeignException.class, () -> service.createRule(mock)),
                () -> verify(authServerProxy, times(1)).getAnalysisById(mock.analysis_id()),
                () -> verify(step2Proxy, times(1)).getControlActionById(mock.control_action_id()),
                () -> verify(step1Proxy, times(1)).getHazardById(mock.hazard_id())
        );
    }

    @Test
    @DisplayName("#createRule > When there is a rule with the given code > Throw an exception")
    void createRuleWhenThereIsARuleWithTheGivenCodeThrowAnException() {
        RuleCreateDto mock = assembleRuleCreate.get();

        when(authServerProxy.getAnalysisById(mock.analysis_id())).thenReturn(null);
        when(step2Proxy.getControlActionById(mock.control_action_id())).thenReturn(null);
        when(step1Proxy.getHazardById(mock.hazard_id())).thenReturn(null);
        when(ruleRepository.findByCode(mock.code())).thenReturn(Optional.of(assembleRule.get()));

        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> service.createRule(mock)),
                () -> verify(authServerProxy, times(1)).getAnalysisById(mock.analysis_id()),
                () -> verify(step2Proxy, times(1)).getControlActionById(mock.control_action_id()),
                () -> verify(step1Proxy, times(1)).getHazardById(mock.hazard_id())
        );
    }

    @Test
    @DisplayName("#createRule > When all given information are correct > Create the rule")
    void createRuleWhenAllGivenInformationAreCorrectCreateTheRule() {
        RuleCreateDto mock = assembleRuleCreate.get();

        when(authServerProxy.getAnalysisById(mock.analysis_id())).thenReturn(null);
        when(step2Proxy.getControlActionById(mock.control_action_id())).thenReturn(null);
        when(step1Proxy.getHazardById(mock.hazard_id())).thenReturn(null);
        when(ruleRepository.findByCode(mock.code())).thenReturn(Optional.empty());
        when(ruleRepository.save(any(Rule.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(mapper.toRuleReadDto(any())).thenAnswer(invocation -> {
            Rule rule = invocation.getArgument(0);
            return RuleReadDto.builder()
                    .id(rule.getId())
                    .code(rule.getCode())
                    .name(rule.getName())
                    .types(rule.getTypes())
                    .build();
        });

        RuleReadDto response = service.createRule(mock);

        assertAll(
                () -> verify(authServerProxy, times(1)).getAnalysisById(mock.analysis_id()),
                () -> verify(step2Proxy, times(1)).getControlActionById(mock.control_action_id()),
                () -> verify(step1Proxy, times(1)).getHazardById(mock.hazard_id()),
                () -> verify(ruleRepository, times(2)).save(any()),
                () -> assertEquals(mock.code(), response.code()),
                () -> assertEquals(mock.name(), response.name()),
                () -> assertEquals(mock.types(), response.types())
        );
    }

    @Test
    @DisplayName("#readRule > When no rule is found > Throw an exception")
    void readRuleWhenNoRuleIsFoundThrowAnException() {
        UUID mockRuleId = UUID.randomUUID();

        when(ruleRepository.findById(mockRuleId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.readRule(mockRuleId));
    }

    @Test
    @DisplayName("#readRule > When the rule is found > Return the rule")
    void readRuleWhenTheRuleIsFoundReturnTheRule() {
        UUID mockRuleId = UUID.randomUUID();
        Rule mockFirst = assembleRule.get();
        RuleReadDto mockRuleReadFirst = fromRuleToReadDTO(mockFirst);

        when(ruleRepository.findById(mockRuleId)).thenReturn(Optional.of(mockFirst));
        when(mapper.toRuleReadDto(mockFirst)).thenReturn(mockRuleReadFirst);

        RuleReadDto response = service.readRule(mockRuleId);

        assertEquals(mockRuleReadFirst, response);
    }
    
    @Test
    @DisplayName("#readAllRules > When no rule is found > Return an empty list")
    void readAllRulesWhenNoRuleIsFoundReturnAnEmptyList() {
        when(ruleRepository.findAll()).thenReturn(List.of());

        List<RuleReadListDto> response = service.readAllRules();

        assertTrue(response.isEmpty());
    }

    @Test
    @DisplayName("#readAllRules > When the rules are found > Return the rules")
    void readAllRulesWhenTheRulesAreFoundReturnTheRules() {
        Rule mockFirst = assembleRule.get();
        Rule mockSecond = assembleRule.get();
        RuleReadListDto mockRuleReadFirst = fromRuleToRuleReadListDTO(mockFirst);
        RuleReadListDto mockRuleReadSecond = fromRuleToRuleReadListDTO(mockSecond);

        when(ruleRepository.findAll()).thenReturn(List.of(mockFirst, mockSecond));
        when(mapper.toRuleReadListDto(mockFirst)).thenReturn(mockRuleReadFirst);
        when(mapper.toRuleReadListDto(mockSecond)).thenReturn(mockRuleReadSecond);

        List<RuleReadListDto> response = service.readAllRules();

        assertAll(
                () -> assertEquals(2, response.size()),
                () -> assertEquals(mockRuleReadFirst, response.getFirst()),
                () -> assertEquals(mockRuleReadSecond, response.getLast())
        );
    }

    @Test
    @DisplayName("#readRulesByControlActionId > When no rule is found > Return an empty list")
    void readRulesByControlActionIdWhenNoRuleIsFoundReturnAnEmptyList() {
        UUID controlActionId = UUID.randomUUID();

        when(ruleRepository.findByControlActionId(controlActionId)).thenReturn(List.of());

        List<RuleReadListDto> response = service.readRulesByControlActionId(controlActionId);

        assertTrue(response.isEmpty());
    }

    @Test
    @DisplayName("#readRulesByControlActionId > When the rules are found > Return the rules")
    void readRulesByControlActionIdWhenTheRulesAreFoundReturnTheRules() {
        UUID controlActionId = UUID.randomUUID();
        Rule mockFirst = assembleRule.get();
        Rule mockSecond = assembleRule.get();
        RuleReadListDto mockRuleReadFirst = fromRuleToRuleReadListDTO(mockFirst);
        RuleReadListDto mockRuleReadSecond = fromRuleToRuleReadListDTO(mockSecond);

        when(ruleRepository.findByControlActionId(controlActionId)).thenReturn(List.of(mockFirst, mockSecond));
        when(mapper.toRuleReadListDto(mockFirst)).thenReturn(mockRuleReadFirst);
        when(mapper.toRuleReadListDto(mockSecond)).thenReturn(mockRuleReadSecond);

        List<RuleReadListDto> response = service.readRulesByControlActionId(controlActionId);

        assertAll(
                () -> assertEquals(2, response.size()),
                () -> assertEquals(mockRuleReadFirst, response.getFirst()),
                () -> assertEquals(mockRuleReadSecond, response.getLast())
        );
    }

    @Test
    @DisplayName("#readRulesByAnalysisId > When no rule is found > Return an empty list")
    void readRulesByAnalysisIdWhenNoRuleIsFoundReturnAnEmptyList() {
        UUID analysisId = UUID.randomUUID();

        when(ruleRepository.findByAnalysisId(analysisId)).thenReturn(List.of());

        List<RuleReadListDto> response = service.readRulesByAnalysisId(analysisId);

        assertTrue(response.isEmpty());
    }

    @Test
    @DisplayName("#readRulesByAnalysisId > When the rules are found > Return the rules")
    void readRulesByAnalysisIdWhenTheRulesAreFoundReturnTheRules() {
        UUID analysisId = UUID.randomUUID();
        Rule mockFirst = assembleRule.get();
        Rule mockSecond = assembleRule.get();
        RuleReadListDto mockRuleReadFirst = fromRuleToRuleReadListDTO(mockFirst);
        RuleReadListDto mockRuleReadSecond = fromRuleToRuleReadListDTO(mockSecond);

        when(ruleRepository.findByAnalysisId(analysisId)).thenReturn(List.of(mockFirst, mockSecond));
        when(mapper.toRuleReadListDto(mockFirst)).thenReturn(mockRuleReadFirst);
        when(mapper.toRuleReadListDto(mockSecond)).thenReturn(mockRuleReadSecond);

        List<RuleReadListDto> response = service.readRulesByAnalysisId(analysisId);

        assertAll(
                () -> assertEquals(2, response.size()),
                () -> assertEquals(mockRuleReadFirst, response.getFirst()),
                () -> assertEquals(mockRuleReadSecond, response.getLast())
        );
    }

    @Test
    @DisplayName("#deleteRule > When the rule does not exist > Throw an exception")
    void deleteRuleWhenTheRuleDoesNotExistThrowAnException() {
        UUID mockRuleId = UUID.randomUUID();

        when(ruleRepository.findById(mockRuleId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.deleteRule(mockRuleId));
    }

    @Test
    @DisplayName("#deleteRule > When the rule exist > When no unsafe control action is found > Delete the rule")
    void deleteRuleWhenTheRuleExistWhenNoUnsafeControlActionIsFoundDeleteTheRule() {
        UUID mockRuleId = UUID.randomUUID();
        Rule mockRule = assembleRule.get();
        List<UnsafeControlAction> mockUnsafeControlActions = new ArrayList<>();

        when(ruleRepository.findById(mockRuleId)).thenReturn(Optional.of(mockRule));
        when(ucaRepository.findByRuleCodeAndAnalysisId(mockRule.getCode(), mockRule.getAnalysisId())).thenReturn(mockUnsafeControlActions);

        service.deleteRule(mockRuleId);

        assertAll(
                () -> verify(ucaRepository, times(1)).deleteAll(mockUnsafeControlActions),
                () -> verify(ruleRepository, times(1)).deleteById(mockRuleId)
        );
    }

    @Test
    @DisplayName("#deleteRule > When the rule and the unsafe control action exists > Delete all UCA and rule")
    void deleteRuleWhenTheRuleAndTheUnsafeControlActionExistsDeleteAllUcaAndRule() {
        UUID mockRuleId = UUID.randomUUID();
        Rule mockRule = assembleRule.get();
        List<UnsafeControlAction> mockUnsafeControlActions = List.of(
                assembleUnsafeControlAction.get(),
                assembleUnsafeControlAction.get()
        );

        when(ruleRepository.findById(mockRuleId)).thenReturn(Optional.of(mockRule));
        when(ucaRepository.findByRuleCodeAndAnalysisId(mockRule.getCode(), mockRule.getAnalysisId())).thenReturn(mockUnsafeControlActions);

        service.deleteRule(mockRuleId);

        assertAll(
                () -> verify(ucaRepository, times(1)).deleteAll(mockUnsafeControlActions),
                () -> verify(ruleRepository, times(1)).deleteById(mockRuleId)
        );
    }

    private RuleReadListDto fromRuleToRuleReadListDTO(Rule rule) {
        return RuleReadListDto.builder()
                .id(rule.getId())
                .name(rule.getName())
                .code(rule.getCode())
                .types(rule.getTypes())
                .build();
    }

    private RuleReadDto fromRuleToReadDTO(Rule rule) {
        return RuleReadDto.builder()
                .id(rule.getId())
                .name(rule.getName())
                .code(rule.getCode())
                .types(rule.getTypes())
                .build();
    }

    private final Supplier<Rule> assembleRule = () -> Rule.builder()
            .id(UUID.randomUUID())
            .name("Rule 01")
            .code("R1")
            .types(new HashSet<>() {{ add(UCAType.NOT_PROVIDED); }})
            .alreadyApplied(Boolean.TRUE)
            .analysisId(UUID.randomUUID())
            .hazardId(UUID.randomUUID())
            .build();

    private final Supplier<UnsafeControlAction> assembleUnsafeControlAction = () -> UnsafeControlAction.builder()
            .id(UUID.randomUUID())
            .hazardId(UUID.randomUUID())
            .type(UCAType.PROVIDED)
            .build();

    private final Supplier<RuleCreateDto> assembleRuleCreate = () -> RuleCreateDto.builder()
            .analysis_id(UUID.randomUUID())
            .control_action_id(UUID.randomUUID())
            .hazard_id(UUID.randomUUID())
            .code("R1")
            .types(new HashSet<>() {{ add(UCAType.NOT_PROVIDED); }})
            .states_ids(List.of())
            .build();

}
