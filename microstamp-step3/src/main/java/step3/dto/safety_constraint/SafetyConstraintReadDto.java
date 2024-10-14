package step3.dto.safety_constraint;



import lombok.Builder;

import java.util.UUID;

@Builder
public record SafetyConstraintReadDto(
        UUID id,
        String name,
        UUID ucaId
) {}
