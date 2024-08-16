package step3.dto.rule;

import jakarta.validation.constraints.*;
import step3.entity.UCAType;
import java.util.List;
import java.util.Set;

public record RuleCreateDto(
        @NotBlank
        String name,
        @NotNull
        Long control_action_id,
        @NotEmpty
        List<Long> values_ids,
        @NotEmpty
        Set<UCAType> types,
        @NotNull
        Long hazard_id
) {}
