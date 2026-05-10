package microstamp.cast.step1.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.cast.step1.dto.systemdescription.SystemDescriptionInsertDto;
import microstamp.cast.step1.dto.systemdescription.SystemDescriptionReadDto;
import microstamp.cast.step1.dto.systemdescription.SystemDescriptionUpdateDto;
import microstamp.cast.step1.exception.Step1NotFoundException;
import microstamp.cast.step1.service.SystemDescriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Log4j2
@RestController
@AllArgsConstructor
@Tag(name = "CAST - System Description")
@RequestMapping("/cast/system-descriptions")
public class SystemDescriptionController {

    private final SystemDescriptionService service;

    @GetMapping
    public ResponseEntity<List<SystemDescriptionReadDto>> findAll() {
        log.info("Request received to find all CAST system descriptions");
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SystemDescriptionReadDto> findById(@PathVariable UUID id) throws Step1NotFoundException {
        log.info("Request received to find CAST system description by id {}", id);
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @GetMapping("/analysis/{id}")
    public ResponseEntity<List<SystemDescriptionReadDto>> findByAnalysisId(@PathVariable UUID id) {
        log.info("Request received to find CAST system descriptions by analysis id {}", id);
        return new ResponseEntity<>(service.findByAnalysisId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SystemDescriptionReadDto> insert(@Valid @RequestBody SystemDescriptionInsertDto systemDescriptionInsertDto) throws Step1NotFoundException {
        log.info("Request received to insert CAST system description {}", systemDescriptionInsertDto);
        return new ResponseEntity<>(service.insert(systemDescriptionInsertDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable UUID id,
                                       @Valid @RequestBody SystemDescriptionUpdateDto systemDescriptionUpdateDto) throws Step1NotFoundException {
        log.info("Request received to update CAST system description with id {}", id);
        service.update(id, systemDescriptionUpdateDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        log.info("Request received to delete CAST system description with id {}", id);
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}