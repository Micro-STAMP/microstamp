package step3.dto.unsafe_control_action;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import step3.entity.UCAType;

import java.util.List;
import java.util.UUID;

@Builder
public record UnsafeControlActionCreateDto(
        @NotNull
        UUID control_action_id,
        @NotEmpty
        List<UUID> states_ids,
        @NotNull
        UUID hazard_id,
        @NotNull
        UCAType type,
        @NotNull
        UUID analysis_id,
        String rule_code
) {}
