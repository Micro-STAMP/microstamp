package step3.dto.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import step3.dto.safety_constraint.SafetyConstraintReadDto;
import step3.entity.SafetyConstraint;
import step3.proxy.Step2Proxy;

@Component
@AllArgsConstructor
public class SafetyConstraintMapper {
    private final Step2Proxy step2Proxy;

    public SafetyConstraintReadDto toSafetyConstraintReadDto(SafetyConstraint safetyConstraint) {
        return SafetyConstraintReadDto.builder()
                .id(safetyConstraint.getId())
                .uca_id(safetyConstraint.getUnsafeControlAction().getId())
                .name(safetyConstraint.getUnsafeControlAction().generateNameTo(step2Proxy, "constraint"))
                .safety_constraint_code(safetyConstraint.getSafetyConstraintCode())
                .build();
    }
}
