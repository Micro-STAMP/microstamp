package microstamp.cast.step1.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.cast.step1.dto.questionraised.QuestionRaisedInsertDto;
import microstamp.cast.step1.dto.questionraised.QuestionRaisedReadDto;
import microstamp.cast.step1.dto.questionraised.QuestionRaisedUpdateDto;
import microstamp.cast.step1.entity.QuestionRaised;
import microstamp.cast.step1.exception.Step1NotFoundException;
import microstamp.cast.step1.mapper.QuestionRaisedMapper;
import microstamp.cast.step1.repository.QuestionRaisedRepository;
import microstamp.cast.step1.service.AuditLogService;
import microstamp.cast.step1.service.QuestionRaisedService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Log4j2
@Service
@AllArgsConstructor
public class QuestionRaisedServiceImpl implements QuestionRaisedService {

    private final QuestionRaisedRepository questionRaisedRepository;

    private final QuestionRaisedMapper questionRaisedMapper;

    private final AuditLogService auditLogService;

    @Override
    @Transactional(readOnly = true)
    public QuestionRaisedReadDto findById(UUID id) {
        log.info("Finding CAST question raised by id: {}", id);
        return questionRaisedMapper.toReadDto(findEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionRaisedReadDto> findByAnalysisId(UUID analysisId, Integer castStep) {
        log.info("Finding CAST questions raised by analysis id {} and step {}", analysisId, castStep);
        List<QuestionRaised> questions = Objects.isNull(castStep)
                ? questionRaisedRepository.findByAnalysisId(analysisId)
                : questionRaisedRepository.findByAnalysisIdAndCastStep(analysisId, castStep);

        return questions.stream()
                .sorted(Comparator.comparing(QuestionRaised::getCastStep)
                        .thenComparing(QuestionRaised::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(questionRaisedMapper::toReadDto)
                .toList();
    }

    @Override
    @Transactional
    public QuestionRaisedReadDto insert(QuestionRaisedInsertDto questionRaisedInsertDto) {
        QuestionRaised questionRaised = questionRaisedMapper.toEntity(questionRaisedInsertDto);

        log.info("Inserting the CAST question raised {} on the database", questionRaised);
        questionRaisedRepository.save(questionRaised);

        auditLogService.record(questionRaised.getAnalysisId(), stepLabel(questionRaised), "Question Raised " + questionRaised.getCode());

        return questionRaisedMapper.toReadDto(questionRaised);
    }

    @Override
    @Transactional
    public QuestionRaisedReadDto update(UUID id, QuestionRaisedUpdateDto questionRaisedUpdateDto) {
        QuestionRaised questionRaised = findEntity(id);
        questionRaisedMapper.updateEntityFromDto(questionRaisedUpdateDto, questionRaised);

        log.info("Updating the CAST question raised with id {}", id);
        questionRaisedRepository.save(questionRaised);

        auditLogService.record(questionRaised.getAnalysisId(), stepLabel(questionRaised), "Question Raised " + questionRaised.getCode());

        return questionRaisedMapper.toReadDto(questionRaised);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        QuestionRaised questionRaised = findEntity(id);

        log.info("Deleting the CAST question raised with id {} on the database", id);
        questionRaisedRepository.delete(questionRaised);
    }

    private QuestionRaised findEntity(UUID id) {
        return questionRaisedRepository.findById(id)
                .orElseThrow(() -> new Step1NotFoundException("QuestionRaised", id.toString()));
    }

    private String stepLabel(QuestionRaised questionRaised) {
        return "Step " + questionRaised.getCastStep();
    }
}
