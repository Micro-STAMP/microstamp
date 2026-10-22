package microstamp.cast.step1.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.cast.step1.dto.questionraised.QuestionRaisedInsertDto;
import microstamp.cast.step1.dto.questionraised.QuestionRaisedReadDto;
import microstamp.cast.step1.dto.questionraised.QuestionRaisedUpdateDto;
import microstamp.cast.step1.service.QuestionRaisedService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Log4j2
@RestController
@AllArgsConstructor
@Tag(name = "CAST - Questions Raised")
@RequestMapping("/cast/questions-raised")
public class QuestionRaisedController {

    private final QuestionRaisedService service;

    @GetMapping("/{id}")
    public ResponseEntity<QuestionRaisedReadDto> findById(@PathVariable(name = "id") UUID id) {
        log.info("Request received to find CAST question raised by id {}", id);
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @GetMapping("/analysis/{id}")
    public ResponseEntity<List<QuestionRaisedReadDto>> findByAnalysisId(@PathVariable(name = "id") UUID id,
                                                                        @RequestParam(name = "step", required = false) Integer castStep) {
        log.info("Request received to find CAST questions raised by analysis id {} (step {})", id, castStep);
        return new ResponseEntity<>(service.findByAnalysisId(id, castStep), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<QuestionRaisedReadDto> insert(@Valid @RequestBody QuestionRaisedInsertDto questionRaisedInsertDto) {
        log.info("Request received to insert CAST question raised with information {}", questionRaisedInsertDto);
        return new ResponseEntity<>(service.insert(questionRaisedInsertDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionRaisedReadDto> update(@PathVariable(name = "id") UUID id,
                                                        @Valid @RequestBody QuestionRaisedUpdateDto questionRaisedUpdateDto) {
        log.info("Request received to update CAST question raised with id {} with information {}", id, questionRaisedUpdateDto);
        return new ResponseEntity<>(service.update(id, questionRaisedUpdateDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") UUID id) {
        log.info("Request received to delete CAST question raised with id {}", id);
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
