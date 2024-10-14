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
        UUID controlActionId,
        @NotEmpty
        List<UUID> statesIds,
        @NotNull
        UUID hazardId,
        @NotNull
        UCAType type,
        @NotNull
        UUID analysisId,
        String ruleCode
) {}
