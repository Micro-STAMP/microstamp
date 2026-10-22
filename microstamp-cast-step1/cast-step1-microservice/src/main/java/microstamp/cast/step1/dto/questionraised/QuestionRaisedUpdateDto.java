package microstamp.cast.step1.dto.questionraised;

import jakarta.validation.constraints.NotBlank;

public record QuestionRaisedUpdateDto(

        @NotBlank
        String code,

        @NotBlank
        String question
) {}
