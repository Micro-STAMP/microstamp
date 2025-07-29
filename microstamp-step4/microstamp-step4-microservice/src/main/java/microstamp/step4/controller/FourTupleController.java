package microstamp.step4.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.step4.dto.fourtuple.FourTupleInsertDto;
import microstamp.step4.dto.fourtuple.FourTupleFullReadDto;
import microstamp.step4.dto.fourtuple.FourTupleUpdateDto;
import microstamp.step4.dto.unsafecontrolaction.UnsafeControlActionFullReadDto;
import microstamp.step4.exception.Step4NotFoundException;
import microstamp.step4.service.FourTupleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Log4j2
@RestController
@AllArgsConstructor
@Tag(name = "FourTuple")
@RequestMapping("/four-tuples")
public class FourTupleController {

    private FourTupleService service;

    @GetMapping
    public ResponseEntity<List<FourTupleFullReadDto>> findAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FourTupleFullReadDto> findById(@PathVariable(name = "id") UUID id) throws Step4NotFoundException {
        log.info("Request received to find 4-tuple by id {}", id);

        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @GetMapping("/analysis/{id}")
    public ResponseEntity<List<FourTupleFullReadDto>> findByAnalysisId(@PathVariable(name = "id") UUID id) {
        log.info("Request received to find 4-tuple by analysis id {}", id);

        return new ResponseEntity<>(service.findByAnalysisId(id), HttpStatus.OK);
    }

    @GetMapping("/analysis/{id}/unsafe-control-actions")
    public ResponseEntity<List<UnsafeControlActionFullReadDto>> findByAnalysisIdSortedByUnsafeControlActions(@PathVariable(name = "id") UUID id) {
        log.info("Request received to find all UCAs and 4-tuples by analysis id {}", id);

        return new ResponseEntity<>(service.findByAnalysisIdSortedByUnsafeControlActions(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<FourTupleFullReadDto> insert(@Valid @RequestBody FourTupleInsertDto fourTupleInsertDto) throws Step4NotFoundException {
        log.info("Request received to insert 4-tuple with information {}", fourTupleInsertDto);

        return new ResponseEntity<>(service.insert(fourTupleInsertDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable(name = "id") UUID id, @Valid @RequestBody FourTupleUpdateDto fourTupleUpdateDto) throws Step4NotFoundException {
        log.info("Request received to update 4-tuple with id {} with information {}", id, fourTupleUpdateDto);

        service.update(id, fourTupleUpdateDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") UUID id) throws Step4NotFoundException {
        log.info("Request received to delete 4-tuple with id {}", id);

        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
