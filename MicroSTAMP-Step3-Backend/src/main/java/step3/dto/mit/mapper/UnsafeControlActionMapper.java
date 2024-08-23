package step3.dto.mit.mapper;

import org.springframework.stereotype.Component;
import step3.dto.mit.unsafe_control_action.UnsafeControlActionReadDto;
import step3.entity.mit.UnsafeControlAction;
import step3.proxy.Step1Proxy;
import step3.proxy.Step2Proxy;

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

        return UnsafeControlActionReadDto.builder()
                .id(uca.getId())
                .name(uca.generateName(step2Proxy))
                .hazard_code(hazardCode)
                .rule_code(uca.getRuleCode())
                .build();
    }
}
