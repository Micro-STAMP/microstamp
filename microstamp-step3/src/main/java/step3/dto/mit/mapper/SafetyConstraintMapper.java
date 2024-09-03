package step3.dto.mit.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import step3.dto.mit.safety_constraint.SafetyConstraintReadDto;
import step3.entity.mit.SafetyConstraint;
import step3.proxy.Step2Proxy;

@Component
@AllArgsConstructor
public class SafetyConstraintMapper {
    private final Step2Proxy step2Proxy;

    public SafetyConstraintReadDto toSafetyConstraintReadDto(SafetyConstraint safetyConstraint) {
        return SafetyConstraintReadDto.builder()
                .id(safetyConstraint.getId())
                .uca_id(safetyConstraint.getUnsafeControlAction().getId())
                .name(safetyConstraint.getUnsafeControlAction().generateConstraintName(step2Proxy))
                .build();
    }
}
