package microstamp.cast.step1.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.cast.step1.dto.casthazard.CastHazardInsertDto;
import microstamp.cast.step1.dto.casthazard.CastHazardReadDto;
import microstamp.cast.step1.dto.casthazard.CastHazardUpdateDto;
import microstamp.cast.step1.exception.Step1NotFoundException;
import microstamp.cast.step1.service.CastHazardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Log4j2
@RestController
@AllArgsConstructor
@Tag(name = "CAST - Hazard")
@RequestMapping("/cast/hazards")
public class CastHazardController {

    private final CastHazardService service;

    @GetMapping
    public ResponseEntity<List<CastHazardReadDto>> findAll() {
        log.info("Request received to find all CAST hazards");
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CastHazardReadDto> findById(@PathVariable(name = "id") UUID id) throws Step1NotFoundException {
        log.info("Request received to find CAST hazard by id {}", id);
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @GetMapping("/analysis/{id}")
    public ResponseEntity<List<CastHazardReadDto>> findByAnalysisId(@PathVariable(name = "id") UUID id) {
        log.info("Request received to find CAST hazards by analysis id {}", id);
        return new ResponseEntity<>(service.findByAnalysisId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CastHazardReadDto> insert(@Valid @RequestBody CastHazardInsertDto castHazardInsertDto) throws Step1NotFoundException {
        log.info("Request received to insert CAST hazard with information {}", castHazardInsertDto);
        return new ResponseEntity<>(service.insert(castHazardInsertDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable(name = "id") UUID id,
                                       @Valid @RequestBody CastHazardUpdateDto castHazardUpdateDto) throws Step1NotFoundException {
        log.info("Request received to update CAST hazard with id {} with information {}", id, castHazardUpdateDto);
        service.update(id, castHazardUpdateDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") UUID id) throws Step1NotFoundException {
        log.info("Request received to delete CAST hazard with id {}", id);
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}