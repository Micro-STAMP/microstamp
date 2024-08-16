package step3.dto.unsafe_control_action;

import jakarta.validation.constraints.*;
import step3.entity.UCAType;

import java.util.List;

public record UnsafeControlActionCreateDto(
        @NotNull
        Long control_action_id,
        @NotEmpty
        List<Long> values_ids,
        @NotNull
        Long hazard_id,
        @NotNull
        UCAType type,
        @NotNull
        Long project_id,
        String rule_tag
) {}
