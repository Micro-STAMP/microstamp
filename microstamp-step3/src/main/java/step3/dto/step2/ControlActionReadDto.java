package step3.dto.step2;

import java.util.UUID;

public record ControlActionReadDto(
        UUID id,
        String name,
        String code,
        ConnectionReadDto connection
) {
}
