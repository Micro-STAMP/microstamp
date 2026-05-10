package microstamp.cast.step1.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.cast.step1.dto.timelineevent.TimelineEventInsertDto;
import microstamp.cast.step1.dto.timelineevent.TimelineEventReadDto;
import microstamp.cast.step1.dto.timelineevent.TimelineEventUpdateDto;
import microstamp.cast.step1.exception.Step1NotFoundException;
import microstamp.cast.step1.service.TimelineEventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Log4j2
@RestController
@AllArgsConstructor
@Tag(name = "CAST - Timeline Event")
@RequestMapping("/cast/timeline-events")
public class TimelineEventController {

    private final TimelineEventService service;

    @GetMapping
    public ResponseEntity<List<TimelineEventReadDto>> findAll() {
        log.info("Request received to find all CAST timeline events");
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimelineEventReadDto> findById(@PathVariable(name = "id") UUID id) throws Step1NotFoundException {
        log.info("Request received to find CAST timeline event by id {}", id);
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @GetMapping("/analysis/{id}")
    public ResponseEntity<List<TimelineEventReadDto>> findByAnalysisId(@PathVariable(name = "id") UUID id) {
        log.info("Request received to find CAST timeline events by analysis id {}", id);
        return new ResponseEntity<>(service.findByAnalysisId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TimelineEventReadDto> insert(@Valid @RequestBody TimelineEventInsertDto timelineEventInsertDto) throws Step1NotFoundException {
        log.info("Request received to insert CAST timeline event with information {}", timelineEventInsertDto);
        return new ResponseEntity<>(service.insert(timelineEventInsertDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable(name = "id") UUID id,
                                       @Valid @RequestBody TimelineEventUpdateDto timelineEventUpdateDto) throws Step1NotFoundException {
        log.info("Request received to update CAST timeline event with id {} with information {}", id, timelineEventUpdateDto);
        service.update(id, timelineEventUpdateDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") UUID id) throws Step1NotFoundException {
        log.info("Request received to delete CAST timeline event with id {}", id);
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}