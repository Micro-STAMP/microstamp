package step3.dto.not_uca_context;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import step3.entity.UCAType;

import java.util.List;
import java.util.UUID;

@Builder
public record NotUcaContextCreateDto(
        @NotNull UUID analysisId,
        @NotNull UUID controlActionId,
        @NotNull UCAType type,
        @NotNull List<UUID> statesIds
) {
}
