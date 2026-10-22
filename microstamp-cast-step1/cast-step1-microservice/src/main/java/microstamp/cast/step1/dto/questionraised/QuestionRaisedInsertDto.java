package microstamp.cast.step1.dto.questionraised;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record QuestionRaisedInsertDto(

        @NotNull
        UUID analysisId,

        @NotNull
        @Min(1)
        @Max(5)
        Integer castStep,

        @NotBlank
        String code,

        @NotBlank
        String question
) {}
