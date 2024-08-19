package step3.service.mit;

import org.springframework.stereotype.Service;
import step3.dto.mit.mapper.RuleMapper;
import step3.dto.mit.rule.RuleCreateDto;
import step3.dto.mit.rule.RuleReadDto;
import step3.dto.mit.rule.RuleReadListDto;
import step3.dto.mit.step1.HazardReadDto;
import step3.dto.mit.step2.ControlActionReadDto;
import step3.dto.mit.step2.StateReadDto;
import step3.entity.mit.Rule;
import step3.entity.mit.association.RuleState;
import step3.proxy.Step1Proxy;
import step3.proxy.Step2Proxy;
import step3.repository.mit.RuleRepository;
import step3.repository.mit.UnsafeControlActionRepository;


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
    private int nextCode;

    public RuleService(
            RuleRepository ruleRepository,
            UnsafeControlActionRepository ucaRepository,
            Step1Proxy step1Proxy,
            Step2Proxy step2Proxy) {
        this.ruleRepository = ruleRepository;
        this.ucaRepository = ucaRepository;
        this.step1Proxy = step1Proxy;
        this.step2Proxy = step2Proxy;
        int ruleListSize = ruleRepository.findAll().size();
        this.nextCode = ruleListSize == 0 ? 1 : ruleListSize + 1;
    }

    // Create -----------------------------------------

    public RuleReadDto createRule(RuleCreateDto ruleCreateDto) {
        List<StateReadDto> states = getRuleStates(ruleCreateDto.values_ids());
        ControlActionReadDto controlAction = step2Proxy.getControlActionById(ruleCreateDto.control_action_id());
        HazardReadDto hazard = step1Proxy.getHazardById(ruleCreateDto.hazard_id());

        Rule rule = Rule.builder()
                .name(ruleCreateDto.name())
                .controlActionId(ruleCreateDto.control_action_id())
                .hazardId(ruleCreateDto.hazard_id())
                .types(ruleCreateDto.types())
                .build();
        rule.setCode(nextCode++);
        Rule createdRule = ruleRepository.save(rule);

        List<RuleState> statesAssociations = new ArrayList<>();
        for (UUID stateId : ruleCreateDto.values_ids()) {
            RuleState ruleState = RuleState.builder()
                    .ruleId(createdRule.getId())
                    .stateId(stateId)
                    .build();
            statesAssociations.add(ruleState);
        }
        createdRule.setStateAssociations(statesAssociations);
        createdRule = ruleRepository.save(createdRule);

        return RuleMapper.toRuleReadDto(createdRule);
    }

    // Read -------------------------------------------

    public RuleReadDto readRule(UUID id) {
        Rule rule = ruleRepository.getReferenceById(id);
        return RuleMapper.toRuleReadDto(rule);
    }

    public List<RuleReadListDto> readAllRules() {
        return ruleRepository.findAll().stream()
                .map(RuleMapper::toRuleReadListDto)
                .toList();
    }

    public List<RuleReadListDto> readRulesByControlActionId(UUID controlActionId) {
        return ruleRepository.findByControlActionId(controlActionId).stream()
                .map(RuleMapper::toRuleReadListDto)
                .toList();
    }

    // Update -----------------------------------------

    // Delete -----------------------------------------

    public void deleteRule(UUID id) {
        var rule = ruleRepository.getReferenceById(id);
        ucaRepository.deleteAll(ucaRepository.findByRuleCode(rule.getCodeName()));
        updateCodesOfUcas(rule.getCode());
        ruleRepository.deleteById(id);
        this.nextCode--;
        updateTags();
    }

    // Methods ----------------------------------------

//    public List<Value> getRuleValues(List<Long> valuesIds) {
//        List<Value> values = new ArrayList<>();
//        for (Long value_id : valuesIds) {
//            Value value = valueRepository.getReferenceById(value_id);
//            values.add(value);
//        }
//        return values;
//    }

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