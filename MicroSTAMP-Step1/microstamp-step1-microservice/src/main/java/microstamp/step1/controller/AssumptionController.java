package microstamp.step1.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.step1.dto.assumption.AssumptionInsertDto;
import microstamp.step1.dto.assumption.AssumptionReadDto;
import microstamp.step1.dto.assumption.AssumptionUpdateDto;
import microstamp.step1.exception.Step1NotFoundException;
import microstamp.step1.service.AssumptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Log4j2
@RestController
@AllArgsConstructor
@Tag(name = "Assumption")
@RequestMapping("/assumptions")
public class AssumptionController {

    private AssumptionService service;

    @GetMapping
    public ResponseEntity<List<AssumptionReadDto>> findAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssumptionReadDto> findById(@PathVariable(name = "id") UUID id) throws Step1NotFoundException {
        log.info("Request received to find assumption by id {}", id);
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @GetMapping("/analysis/{id}")
    public ResponseEntity<List<AssumptionReadDto>> findByAnalysisId(@PathVariable(name = "id") UUID id) {
        log.info("Request received to find assumption by analysis id {}", id);

        return new ResponseEntity<>(service.findByAnalysisId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AssumptionReadDto> insert(@Valid @RequestBody AssumptionInsertDto assumptionInsertDto) throws Step1NotFoundException {
        log.info("Request received to insert assumption with information {}", assumptionInsertDto);

        return new ResponseEntity<>(service.insert(assumptionInsertDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable(name = "id") UUID id, @Valid @RequestBody AssumptionUpdateDto assumptionUpdateDto) throws Step1NotFoundException {
        log.info("Request received to update assumption with id {} with information {}", id, assumptionUpdateDto);

        service.update(id, assumptionUpdateDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") UUID id) throws Step1NotFoundException {
        log.info("Request received to delete assumption with id {}", id);

        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
