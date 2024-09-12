package step3.dto.mapper;

import org.springframework.stereotype.Component;
import step3.dto.step2.StateReadDto;
import step3.dto.unsafe_control_action.UnsafeControlActionReadDto;
import step3.entity.UnsafeControlAction;
import step3.entity.association.UnsafeControlActionState;
import step3.proxy.Step1Proxy;
import step3.proxy.Step2Proxy;

import java.util.List;

@Component
public class UnsafeControlActionMapper {
    private final Step1Proxy step1Proxy;
    private final Step2Proxy step2Proxy;

    public UnsafeControlActionMapper(Step1Proxy step1Proxy, Step2Proxy step2Proxy) {
        this.step1Proxy = step1Proxy;
        this.step2Proxy = step2Proxy;
    }

    public UnsafeControlActionReadDto toUcaReadDto(UnsafeControlAction uca) {
        String hazardCode = step1Proxy.getHazardById(uca.getHazardId()).code();
        List<StateReadDto> states = getStatesByUca(uca);

        return UnsafeControlActionReadDto.builder()
                .id(uca.getId())
                .name(uca.generateName(step2Proxy))
                .hazard_code(hazardCode)
                .rule_code(uca.getRuleCode())
                .type(uca.getType().toString())
                .states(states)
                .build();
    }

    private List<StateReadDto> getStatesByUca(UnsafeControlAction uca) {
        return uca.getStateAssociations().stream()
                .map(UnsafeControlActionState::getStateId)
                .map(step2Proxy::getStateById)
                .toList();
    }
}
