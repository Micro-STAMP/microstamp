package step3.dto.mit.mapper;

import step3.dto.mit.unsafe_control_action.UnsafeControlActionReadDto;
import step3.entity.mit.UnsafeControlAction;
import step3.proxy.Step1Proxy;
import step3.proxy.Step2Proxy;

public class UnsafeControlActionMapper {
    private static Step1Proxy step1Proxy;
    private static Step2Proxy step2Proxy;

    public static UnsafeControlActionReadDto toUcaReadDto(UnsafeControlAction uca) {
        String hazardCode = step1Proxy.getHazardById(uca.getHazardId()).code();

        return UnsafeControlActionReadDto.builder()
                .id(uca.getId())
                .name(uca.getName())
                .hazard_code(hazardCode)
                .rule_code(uca.getRuleCode())
                .build();
    }
}
