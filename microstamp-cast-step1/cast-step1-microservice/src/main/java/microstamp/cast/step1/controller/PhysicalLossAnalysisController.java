package microstamp.cast.step1.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.cast.step1.dto.physicallossanalysis.PhysicalLossAnalysisInsertDto;
import microstamp.cast.step1.dto.physicallossanalysis.PhysicalLossAnalysisReadDto;
import microstamp.cast.step1.dto.physicallossanalysis.PhysicalLossAnalysisUpdateDto;
import microstamp.cast.step1.exception.Step1NotFoundException;
import microstamp.cast.step1.service.PhysicalLossAnalysisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Log4j2
@RestController
@AllArgsConstructor
@Tag(name = "CAST - Physical Loss Analysis")
@RequestMapping("/cast/physical-loss-analyses")
public class PhysicalLossAnalysisController {

    private final PhysicalLossAnalysisService service;

    @GetMapping
    public ResponseEntity<List<PhysicalLossAnalysisReadDto>> findAll() {
        log.info("Request received to find all CAST physical loss analyses");
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhysicalLossAnalysisReadDto> findById(@PathVariable(name = "id") UUID id) throws Step1NotFoundException {
        log.info("Request received to find CAST physical loss analysis by id {}", id);
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @GetMapping("/analysis/{id}")
    public ResponseEntity<List<PhysicalLossAnalysisReadDto>> findByAnalysisId(@PathVariable(name = "id") UUID id) {
        log.info("Request received to find CAST physical loss analyses by analysis id {}", id);
        return new ResponseEntity<>(service.findByAnalysisId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PhysicalLossAnalysisReadDto> insert(@Valid @RequestBody PhysicalLossAnalysisInsertDto physicalLossAnalysisInsertDto) throws Step1NotFoundException {
        log.info("Request received to insert CAST physical loss analysis with information {}", physicalLossAnalysisInsertDto);
        return new ResponseEntity<>(service.insert(physicalLossAnalysisInsertDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable(name = "id") UUID id,
                                       @Valid @RequestBody PhysicalLossAnalysisUpdateDto physicalLossAnalysisUpdateDto) throws Step1NotFoundException {
        log.info("Request received to update CAST physical loss analysis with id {} with information {}", id, physicalLossAnalysisUpdateDto);
        service.update(id, physicalLossAnalysisUpdateDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") UUID id) throws Step1NotFoundException {
        log.info("Request received to delete CAST physical loss analysis with id {}", id);
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}