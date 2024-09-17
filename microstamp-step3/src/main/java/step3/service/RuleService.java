package step3.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import step3.dto.mapper.RuleMapper;
import step3.dto.rule.RuleCreateDto;
import step3.dto.rule.RuleReadDto;
import step3.dto.rule.RuleReadListDto;
import step3.dto.step1.HazardReadDto;
import step3.dto.step2.ControlActionReadDto;
import step3.dto.step2.StateReadDto;
import step3.entity.Rule;
import step3.entity.UnsafeControlAction;
import step3.entity.association.RuleState;
import step3.proxy.AuthServerProxy;
import step3.proxy.Step1Proxy;
import step3.proxy.Step2Proxy;
import step3.repository.RuleRepository;
import step3.repository.UnsafeControlActionRepository;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
//@AllArgsConstructor
public class RuleService {
    private final RuleRepository ruleRepository;
    private final UnsafeControlActionRepository ucaRepository;
    private final Step1Proxy step1Proxy;
    private final Step2Proxy step2Proxy;
    private final AuthServerProxy authServerProxy;
    private final RuleMapper mapper;
    private int nextCode;

    public RuleService(
            RuleRepository ruleRepository,
            UnsafeControlActionRepository ucaRepository,
            Step1Proxy step1Proxy,
            Step2Proxy step2Proxy,
            AuthServerProxy authServerProxy, RuleMapper mapper) {
        this.ruleRepository = ruleRepository;
        this.ucaRepository = ucaRepository;
        this.step1Proxy = step1Proxy;
        this.step2Proxy = step2Proxy;
        this.authServerProxy = authServerProxy;
        this.mapper = mapper;
        int ruleListSize = ruleRepository.findAll().size();
        this.nextCode = ruleListSize == 0 ? 1 : ruleListSize + 1;
    }

    public RuleReadDto createRule(RuleCreateDto ruleCreateDto) {
        authServerProxy.getAnalysisById(ruleCreateDto.analysis_id());
        step2Proxy.getControlActionById(ruleCreateDto.control_action_id());
        step1Proxy.getHazardById(ruleCreateDto.hazard_id());

        Rule rule = Rule.builder()
                .name(ruleCreateDto.name())
                .controlActionId(ruleCreateDto.control_action_id())
                .analysisId(ruleCreateDto.analysis_id())
                .hazardId(ruleCreateDto.hazard_id())
                .types(ruleCreateDto.types())
                .build();
        rule.setCode(nextCode++);
        Rule createdRule = ruleRepository.save(rule);

        List<RuleState> statesAssociations = new ArrayList<>();
        for (UUID stateId : ruleCreateDto.states_ids()) {
            step2Proxy.getStateById(stateId);

            RuleState ruleState = RuleState.builder()
                    .rule(createdRule)
                    .stateId(stateId)
                    .build();
            statesAssociations.add(ruleState);
        }

        createdRule.setStateAssociations(statesAssociations);
        createdRule = ruleRepository.save(createdRule);

        return mapper.toRuleReadDto(createdRule);
    }

    public RuleReadDto readRule(UUID id) {
        Rule rule = ruleRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rule not found with id " + id));

        return mapper.toRuleReadDto(rule);
    }

    public List<RuleReadListDto> readAllRules() {
        return ruleRepository.findAll().stream()
                .map(mapper::toRuleReadListDto)
                .toList();
    }

    public List<RuleReadListDto> readRulesByControlActionId(UUID controlActionId) {
        return ruleRepository.findByControlActionId(controlActionId).stream()
                .map(mapper::toRuleReadListDto)
                .toList();
    }

    public List<RuleReadListDto> readRulesByAnalysisId(UUID analysisId) {
        return ruleRepository.findByAnalysisId(analysisId).stream()
                .map(mapper::toRuleReadListDto)
                .toList();
    }

    public void deleteRule(UUID id) {
        Rule rule = ruleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rule not found with id " + id));

        List<UnsafeControlAction> ucaList = ucaRepository.findByRuleCode(rule.getCodeName());
        ucaRepository.deleteAll(ucaList);

        updateCodesOfUcas(rule.getCode());
        ruleRepository.deleteById(id);
        this.nextCode--;
        updateTags();
    }

    public List<StateReadDto> getRuleStates(List<UUID> statesIds) {
        List<StateReadDto> states = new ArrayList<>();
        for (UUID stateId : statesIds) {
            StateReadDto state = step2Proxy.getStateById(stateId);
            states.add(state);
        }
        return states;
    }

    private void updateTags() {
        var rules = ruleRepository.findAll();
        AtomicInteger newCode = new AtomicInteger(1);
        rules.forEach(rule -> rule.setCode(newCode.getAndIncrement()));
    }

    private void updateCodesOfUcas(int deletedRuleTag) {
        var ucaList = ucaRepository.findAll();
        ucaList.forEach(uca -> {
            String replacedUcaTag = uca.getRuleCode().replace("R", "");
            int ucaTagIndex = replacedUcaTag.isEmpty() ? -99 : Integer.parseInt(replacedUcaTag);
            if (ucaTagIndex > deletedRuleTag) {
                uca.setRuleCode("R" + (ucaTagIndex - 1));
                ucaRepository.save(uca);
            }
        });
    }
}
