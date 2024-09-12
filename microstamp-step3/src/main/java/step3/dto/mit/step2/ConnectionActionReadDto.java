package step3.dto.mit.step2;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ConnectionActionReadDto(
        UUID id,
        String code,
        String name,
        ConnectionActionType connectionActionType
) {
}
