package step3.dto.auth;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record AnalysisReadDto(
        UUID id,
        String name,
        String description,
        Instant creationDate,
        UUID userId
) {
}
