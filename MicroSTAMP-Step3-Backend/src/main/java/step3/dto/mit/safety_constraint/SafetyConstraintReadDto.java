package step3.dto.mit.safety_constraint;



import step3.entity.mit.SafetyConstraint;
import java.util.UUID;

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
