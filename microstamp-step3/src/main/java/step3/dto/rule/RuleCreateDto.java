package step3.dto.rule;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import step3.entity.UCAType;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Builder
public record RuleCreateDto(
        @NotBlank
        String name,
        @NotNull
        UUID analysisId,
        @NotNull
        UUID controlActionId,
        @NotEmpty
        List<UUID> statesIds,
        @NotEmpty
        Set<UCAType> types,
        @NotNull
        UUID hazardId,
        String code
) {}
