package microstamp.cast.step1.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.cast.step1.dto.violatedsystemsafetyconstraint.ViolatedSystemSafetyConstraintInsertDto;
import microstamp.cast.step1.dto.violatedsystemsafetyconstraint.ViolatedSystemSafetyConstraintReadDto;
import microstamp.cast.step1.dto.violatedsystemsafetyconstraint.ViolatedSystemSafetyConstraintUpdateDto;
import microstamp.cast.step1.exception.Step1NotFoundException;
import microstamp.cast.step1.service.ViolatedSystemSafetyConstraintService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Log4j2
@RestController
@AllArgsConstructor
@Tag(name = "CAST - Violated System Safety Constraint")
@RequestMapping("/cast/violated-system-safety-constraints")
public class ViolatedSystemSafetyConstraintController {

    private final ViolatedSystemSafetyConstraintService service;

    @GetMapping
    public ResponseEntity<List<ViolatedSystemSafetyConstraintReadDto>> findAll() {
        log.info("Request received to find all CAST violated system safety constraints");
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ViolatedSystemSafetyConstraintReadDto> findById(@PathVariable(name = "id") UUID id) throws Step1NotFoundException {
        log.info("Request received to find CAST violated system safety constraint by id {}", id);
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @GetMapping("/analysis/{id}")
    public ResponseEntity<List<ViolatedSystemSafetyConstraintReadDto>> findByAnalysisId(@PathVariable(name = "id") UUID id) {
        log.info("Request received to find CAST violated system safety constraints by analysis id {}", id);
        return new ResponseEntity<>(service.findByAnalysisId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ViolatedSystemSafetyConstraintReadDto> insert(
            @Valid @RequestBody ViolatedSystemSafetyConstraintInsertDto violatedSystemSafetyConstraintInsertDto
    ) throws Step1NotFoundException {
        log.info("Request received to insert CAST violated system safety constraint with information {}", violatedSystemSafetyConstraintInsertDto);
        return new ResponseEntity<>(service.insert(violatedSystemSafetyConstraintInsertDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable(name = "id") UUID id,
            @Valid @RequestBody ViolatedSystemSafetyConstraintUpdateDto violatedSystemSafetyConstraintUpdateDto
    ) throws Step1NotFoundException {
        log.info("Request received to update CAST violated system safety constraint with id {} with information {}", id, violatedSystemSafetyConstraintUpdateDto);
        service.update(id, violatedSystemSafetyConstraintUpdateDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") UUID id) throws Step1NotFoundException {
        log.info("Request received to delete CAST violated system safety constraint with id {}", id);
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}