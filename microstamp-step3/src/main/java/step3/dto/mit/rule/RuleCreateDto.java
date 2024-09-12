package step3.dto.mit.rule;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import step3.entity.mit.UCAType;

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
        List<UUID> values_ids,
        @NotEmpty
        Set<UCAType> types,
        @NotNull
        UUID hazard_id
) {}
