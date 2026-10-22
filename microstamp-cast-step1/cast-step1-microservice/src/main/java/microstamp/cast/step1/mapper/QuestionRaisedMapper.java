package microstamp.cast.step1.mapper;

import microstamp.cast.step1.dto.questionraised.QuestionRaisedInsertDto;
import microstamp.cast.step1.dto.questionraised.QuestionRaisedReadDto;
import microstamp.cast.step1.dto.questionraised.QuestionRaisedUpdateDto;
import microstamp.cast.step1.entity.QuestionRaised;
import org.springframework.stereotype.Component;

@Component
public class QuestionRaisedMapper {

    public QuestionRaisedReadDto toReadDto(QuestionRaised entity) {
        return new QuestionRaisedReadDto(
                entity.getId(),
                entity.getAnalysisId(),
                entity.getCastStep(),
                entity.getCode(),
                entity.getQuestion(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public QuestionRaised toEntity(QuestionRaisedInsertDto dto) {
        return QuestionRaised.builder()
                .analysisId(dto.analysisId())
                .castStep(dto.castStep())
                .code(dto.code())
                .question(dto.question())
                .build();
    }

    public void updateEntityFromDto(QuestionRaisedUpdateDto dto, QuestionRaised entity) {
        entity.setCode(dto.code());
        entity.setQuestion(dto.question());
    }
}
