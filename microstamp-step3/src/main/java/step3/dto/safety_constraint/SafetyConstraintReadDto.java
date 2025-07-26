package step3.dto.safety_constraint;



import lombok.Builder;
import step3.entity.SafetyConstraint;
import java.util.UUID;

@Builder
public record SafetyConstraintReadDto(
        UUID id,
        String name,
        String safety_constraint_code,
        UUID uca_id
) {}
