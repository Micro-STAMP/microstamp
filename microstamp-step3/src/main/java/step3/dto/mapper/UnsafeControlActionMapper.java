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
                .analysis_id(uca.getAnalysisId())
                .name(uca.generateNameTo(step2Proxy, "uca"))
                .hazard_code(hazardCode)
                .rule_code(uca.getRuleCode())
                .uca_code(uca.getUcaCode())
                .type(uca.getType().toString())
                .states(states)
                .constraintName(uca.generateNameTo(step2Proxy, "constraint"))
                .constraint_code(uca.getConstraint().getSafetyConstraintCode())
                .constraint_id(uca.getConstraint().getId())
                .build();
    }

    public List<UnsafeControlActionReadDto> toUcaReadDtoList(List<UnsafeControlAction> ucas) {
        return ucas.stream()
                .map(this::toUcaReadDto)
                .toList();
    }

    private List<StateReadDto> getStatesByUca(UnsafeControlAction uca) {
        return uca.getStateAssociations().stream()
                .map(UnsafeControlActionState::getStateId)
                .map(step2Proxy::getStateById)
                .toList();
    }
}
