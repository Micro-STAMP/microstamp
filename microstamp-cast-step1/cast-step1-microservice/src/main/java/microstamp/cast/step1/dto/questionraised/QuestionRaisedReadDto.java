package microstamp.cast.step1.dto.questionraised;

import java.time.Instant;
import java.util.UUID;

public record QuestionRaisedReadDto(
        UUID id,
        UUID analysisId,
        Integer castStep,
        String code,
        String question,
        Instant createdAt,
        Instant updatedAt
) {}
