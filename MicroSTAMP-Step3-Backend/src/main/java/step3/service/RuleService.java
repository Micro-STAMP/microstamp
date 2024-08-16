package step3.service;

import org.springframework.stereotype.Service;
import step3.dto.rule.*;
import step3.entity.*;
import step3.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
//@AllArgsConstructor
public class RuleService {
    private final RuleRepository ruleRepository;
    private final ControlActionRepository controlActionRepository;
    private final ValueRepository valueRepository;
    private final HazardRepository hazardRepository;
    private final UnsafeControlActionRepository ucaRepository;
    private int nextTag;

    public RuleService(RuleRepository ruleRepository,
                       ControlActionRepository controlActionRepository,
                       ValueRepository valueRepository,
                       HazardRepository hazardRepository, UnsafeControlActionRepository ucaRepository) {
        this.ruleRepository = ruleRepository;
        this.controlActionRepository = controlActionRepository;
        this.valueRepository = valueRepository;
        this.hazardRepository = hazardRepository;
        this.ucaRepository = ucaRepository;

        int ruleListSize = ruleRepository.findAll().size();
        this.nextTag = ruleListSize == 0 ? 1 : ruleListSize + 1;
    }

    // Create -----------------------------------------

    public RuleReadDto createRule(RuleCreateDto ruleCreateDto) {
        List<Value> values = getRuleValues(ruleCreateDto.values_ids());
        ControlAction controlAction = controlActionRepository.getReferenceById(ruleCreateDto.control_action_id());
        Hazard hazard = hazardRepository.getReferenceById(ruleCreateDto.hazard_id());

        Rule rule = new Rule(ruleCreateDto.name(), controlAction, values, hazard, ruleCreateDto.types());
        rule.setTag(nextTag++);
        Rule createdRule = ruleRepository.save(rule);
        return new RuleReadDto(createdRule);
    }

    // Read -------------------------------------------

    public RuleReadDto readRule(Long id) {
        Rule rule = ruleRepository.getReferenceById(id);
        return new RuleReadDto(rule);
    }

    public List<RuleReadListDto> readAllRules() {
        return ruleRepository.findAll().stream().map(RuleReadListDto::new).toList();
    }

    public List<RuleReadListDto> readRulesByControlActionId(Long controlActionId) {
        return ruleRepository.findByControlActionId(controlActionId).stream().map(RuleReadListDto::new).toList();
    }

    // Update -----------------------------------------

    // Delete -----------------------------------------

    public void deleteRule(Long id) {
        var rule = ruleRepository.getReferenceById(id);
        ucaRepository.deleteAll(ucaRepository.findByRuleTag(rule.getTagName()));
        updateTagsOfUcas(rule.getTag());
        ruleRepository.deleteById(id);
        this.nextTag--;
        updateTags();
    }

    // Methods ----------------------------------------

    public List<Value> getRuleValues(List<Long> valuesIds) {
        List<Value> values = new ArrayList<>();
        for (Long value_id : valuesIds) {
            Value value = valueRepository.getReferenceById(value_id);
            values.add(value);
        }
        return values;
    }

    private void updateTags() {
        var rules = ruleRepository.findAll();
        AtomicInteger newTag = new AtomicInteger(1);
        rules.forEach(rule -> rule.setTag(newTag.getAndIncrement()));
    }

    private void updateTagsOfUcas(int deletedRuleTag) {
        var ucaList = ucaRepository.findAll();
        ucaList.forEach(uca -> {
            String replacedUcaTag = uca.getRuleTag().replace("R", "");
            int ucaTagIndex = replacedUcaTag.isEmpty() ? -99 : Integer.parseInt(replacedUcaTag);
            if (ucaTagIndex > deletedRuleTag) {
                uca.setRuleTag("R" + (ucaTagIndex - 1));
                ucaRepository.save(uca);
            }
        });
    }
}
