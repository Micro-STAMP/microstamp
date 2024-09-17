package step3.dto.rule;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import step3.entity.UCAType;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public record RuleCreateDto(
        @NotBlank
        String name,
        @NotNull
        UUID analysis_id,
        @NotNull
        UUID control_action_id,
        @NotEmpty
        List<UUID> states_ids,
        @NotEmpty
        Set<UCAType> types,
        @NotNull
        UUID hazard_id,
        String code
) {}
