package step3.dto.mit.step2;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ControlActionReadDto(
        UUID id,
        String name,
        String code
) {
}
