package step3.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import step3.dto.mapper.RuleMapper;
import step3.dto.rule.RuleCreateDto;
import step3.dto.rule.RuleReadDto;
import step3.dto.rule.RuleReadListDto;
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

@Service
@AllArgsConstructor
public class RuleService {
    private final RuleRepository ruleRepository;
    private final UnsafeControlActionRepository ucaRepository;
    private final Step1Proxy step1Proxy;
    private final Step2Proxy step2Proxy;
    private final AuthServerProxy authServerProxy;
    private final RuleMapper mapper;

    public RuleReadDto createRule(RuleCreateDto ruleCreateDto) {
        authServerProxy.getAnalysisById(ruleCreateDto.analysis_id());
        step2Proxy.getControlActionById(ruleCreateDto.control_action_id());
        step1Proxy.getHazardById(ruleCreateDto.hazard_id());

        if (ruleRepository.findByCode(ruleCreateDto.code()).isPresent()) {
            throw new IllegalArgumentException("Rule with code " + ruleCreateDto.code() + " already exists");
        }

        Rule rule = Rule.builder()
                .name(ruleCreateDto.name())
                .controlActionId(ruleCreateDto.control_action_id())
                .analysisId(ruleCreateDto.analysis_id())
                .hazardId(ruleCreateDto.hazard_id())
                .types(ruleCreateDto.types())
                .code(ruleCreateDto.code())
                .build();

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
        Rule rule = ruleRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rule not found with id " + id));

        List<UnsafeControlAction> ucaList = ucaRepository
                .findByRuleCodeAndAnalysisId(rule.getCode(), rule.getAnalysisId());
        ucaRepository.deleteAll(ucaList);

        ruleRepository.deleteById(id);
    }

    public List<StateReadDto> getRuleStates(List<UUID> statesIds) {
        List<StateReadDto> states = new ArrayList<>();
        for (UUID stateId : statesIds) {
            StateReadDto state = step2Proxy.getStateById(stateId);
            states.add(state);
        }
        return states;
    }
}
