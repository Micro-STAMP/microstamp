package step3.dto.step2;

import lombok.Builder;

import java.util.UUID;

@Builder
public record InteractionReadDto(
        UUID id,
        String code,
        String name,
        InteractionType interactionType
) {
}
