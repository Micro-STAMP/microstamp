package step3.dto.step2;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record ConnectionReadDto(
        UUID id,
        String code,
        ComponentReadDto source,
        ComponentReadDto target,
        ConnectionStyle style,
        List<ConnectionActionReadDto> connectionActions
) {
}
