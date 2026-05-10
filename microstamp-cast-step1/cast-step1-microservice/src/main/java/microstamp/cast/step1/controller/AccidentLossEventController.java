package microstamp.cast.step1.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.cast.step1.dto.accidentlossevent.AccidentLossEventInsertDto;
import microstamp.cast.step1.dto.accidentlossevent.AccidentLossEventReadDto;
import microstamp.cast.step1.dto.accidentlossevent.AccidentLossEventUpdateDto;
import microstamp.cast.step1.exception.Step1NotFoundException;
import microstamp.cast.step1.service.AccidentLossEventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Log4j2
@RestController
@AllArgsConstructor
@Tag(name = "CAST - Accident / Loss Event")
@RequestMapping("/cast/accident-loss-events")
public class AccidentLossEventController {

    private final AccidentLossEventService service;

    @GetMapping
    public ResponseEntity<List<AccidentLossEventReadDto>> findAll() {
        log.info("Request received to find all CAST accident/loss events");
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccidentLossEventReadDto> findById(@PathVariable(name = "id") UUID id) throws Step1NotFoundException {
        log.info("Request received to find CAST accident/loss event by id {}", id);
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @GetMapping("/analysis/{id}")
    public ResponseEntity<List<AccidentLossEventReadDto>> findByAnalysisId(@PathVariable(name = "id") UUID id) {
        log.info("Request received to find CAST accident/loss events by analysis id {}", id);
        return new ResponseEntity<>(service.findByAnalysisId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AccidentLossEventReadDto> insert(@Valid @RequestBody AccidentLossEventInsertDto dto) throws Step1NotFoundException {
        log.info("Request received to insert CAST accident/loss event with information {}", dto);
        return new ResponseEntity<>(service.insert(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable(name = "id") UUID id,
                                       @Valid @RequestBody AccidentLossEventUpdateDto dto) throws Step1NotFoundException {
        log.info("Request received to update CAST accident/loss event with id {} with information {}", id, dto);
        service.update(id, dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") UUID id) throws Step1NotFoundException {
        log.info("Request received to delete CAST accident/loss event with id {}", id);
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}