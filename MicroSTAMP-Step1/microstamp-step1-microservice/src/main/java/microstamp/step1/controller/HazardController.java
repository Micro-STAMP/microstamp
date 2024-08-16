package microstamp.step1.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.step1.dto.hazard.HazardInsertDto;
import microstamp.step1.dto.hazard.HazardReadDto;
import microstamp.step1.dto.hazard.HazardUpdateDto;
import microstamp.step1.exception.Step1NotFoundException;
import microstamp.step1.service.HazardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Log4j2
@RestController
@AllArgsConstructor
@Tag(name = "Hazard")
@RequestMapping("/hazards")
public class HazardController {

    private HazardService service;

    @GetMapping
    public ResponseEntity<List<HazardReadDto>> findAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HazardReadDto> findById(@PathVariable(name = "id") UUID id) throws Step1NotFoundException {
        log.info("Request received to find hazard by id {}", id);

        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @GetMapping("/analysis/{id}")
    public ResponseEntity<List<HazardReadDto>> findByAnalysisId(@PathVariable(name = "id") UUID id) {
        log.info("Request received to find hazard by analysis id {}", id);

        return new ResponseEntity<>(service.findByAnalysisId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HazardReadDto> insert(@Valid @RequestBody HazardInsertDto hazardInsertDto) throws Step1NotFoundException {
        log.info("Request received to insert hazard with information {}", hazardInsertDto);

        return new ResponseEntity<>(service.insert(hazardInsertDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable(name = "id") UUID id, @Valid @RequestBody HazardUpdateDto hazardUpdateDto) throws Step1NotFoundException {
        log.info("Request received to update hazard with id {} with information {}", id, hazardUpdateDto);

        service.update(id, hazardUpdateDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") UUID id) throws Step1NotFoundException {
        log.info("Request received to delete hazard with id {}", id);

        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = {"{id}/children"})
    public ResponseEntity<List<HazardReadDto>> getComponentChildren(@PathVariable UUID id) throws Step1NotFoundException {
        log.info("Request received to find all children for hazard with id {}", id);

        List<HazardReadDto> list = service.getHazardChildren(id);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

}
