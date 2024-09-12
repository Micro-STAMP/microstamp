package step3.dto.safety_constraint;



import lombok.Builder;
import step3.entity.SafetyConstraint;
import java.util.UUID;

@Builder
public record SafetyConstraintReadDto(
        UUID id,
        String name,
        UUID uca_id
) {

    // Constructors -----------------------------------

    public SafetyConstraintReadDto(SafetyConstraint safetyConstraint) {
        this(
            safetyConstraint.getId(),
            safetyConstraint.getName(),
            safetyConstraint.getUnsafeControlAction().getId()
        );
    }

    // ------------------------------------------------
}
