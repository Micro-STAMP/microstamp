package microstamp.cast.step1.service;

import microstamp.cast.step1.dto.questionraised.QuestionRaisedInsertDto;
import microstamp.cast.step1.dto.questionraised.QuestionRaisedReadDto;
import microstamp.cast.step1.dto.questionraised.QuestionRaisedUpdateDto;

import java.util.List;
import java.util.UUID;

public interface QuestionRaisedService {

    QuestionRaisedReadDto findById(UUID id);

    List<QuestionRaisedReadDto> findByAnalysisId(UUID analysisId, Integer castStep);

    QuestionRaisedReadDto insert(QuestionRaisedInsertDto questionRaisedInsertDto);

    QuestionRaisedReadDto update(UUID id, QuestionRaisedUpdateDto questionRaisedUpdateDto);

    void delete(UUID id);
}
