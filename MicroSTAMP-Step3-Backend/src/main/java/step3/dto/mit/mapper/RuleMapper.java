package step3.dto.mit.mapper;

import step3.dto.mit.rule.RuleReadDto;
import step3.dto.mit.rule.RuleReadListDto;
import step3.dto.mit.step1.HazardReadDto;
import step3.dto.mit.step2.ControlActionReadDto;
import step3.dto.mit.step2.StateReadDto;
import step3.entity.mit.Rule;
import step3.entity.mit.association.RuleState;
import step3.proxy.Step1Proxy;
import step3.proxy.Step2Proxy;

import java.util.List;

public class RuleMapper {
    private static Step1Proxy step1Proxy;
    private static Step2Proxy step2Proxy;

    public static RuleReadDto toRuleReadDto(Rule rule) {
        HazardReadDto hazard = step1Proxy.getHazardById(rule.getHazardId());
        ControlActionReadDto controlAction = step2Proxy.getControlActionById(rule.getControlActionId());
        List<StateReadDto> states = getStatesByRule(rule);


        return RuleReadDto.builder()
                .id(rule.getId())
                .name(rule.getName())
                .types(rule.getTypes())
                .hazard(hazard)
                .control_action(controlAction)
                .code(rule.getCodeName())
                .states(states)
                .build();
    }

    public static RuleReadListDto toRuleReadListDto(Rule rule) {
        HazardReadDto hazard = step1Proxy.getHazardById(rule.getHazardId());
        ControlActionReadDto controlAction = step2Proxy.getControlActionById(rule.getControlActionId());
        List<StateReadDto> states = getStatesByRule(rule);


        return RuleReadListDto.builder()
                .id(rule.getId())
                .name(rule.getName())
                .types(rule.getTypes())
                .hazard(hazard)
                .control_action_name(controlAction.name())
                .code(rule.getCodeName())
                .states(states)
                .build();
    }



    private static List<StateReadDto> getStatesByRule(Rule rule) {
        return rule.getStateAssociations().stream()
                .map(RuleState::getStateId)
                .map(step2Proxy::getStateById)
                .toList();
    }
}
