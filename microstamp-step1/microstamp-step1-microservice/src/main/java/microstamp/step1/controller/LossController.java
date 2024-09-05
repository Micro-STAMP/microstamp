package microstamp.step1.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.step1.dto.loss.LossInsertDto;
import microstamp.step1.dto.loss.LossReadDto;
import microstamp.step1.dto.loss.LossUpdateDto;
import microstamp.step1.exception.Step1NotFoundException;
import microstamp.step1.service.LossService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Log4j2
@RestController
@AllArgsConstructor
@Tag(name = "Loss")
@RequestMapping("/losses")
public class LossController {

    private LossService service;

    @GetMapping
    public ResponseEntity<List<LossReadDto>> findAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LossReadDto> findById(@PathVariable(name = "id") UUID id) throws Step1NotFoundException {
        log.info("Request received to find loss by id {}", id);

        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @GetMapping("/analysis/{id}")
    public ResponseEntity<List<LossReadDto>> findByAnalysisId(@PathVariable(name = "id") UUID id) {
        log.info("Request received to find loss by analysis id {}", id);

        return new ResponseEntity<>(service.findByAnalysisId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<LossReadDto> insert(@Valid @RequestBody LossInsertDto lossInsertDto) throws Step1NotFoundException {
        log.info("Request received to insert loss with information {}", lossInsertDto);

        return new ResponseEntity<>(service.insert(lossInsertDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable(name = "id") UUID id, @Valid @RequestBody LossUpdateDto lossUpdateDto) throws Step1NotFoundException {
        log.info("Request received to update loss with id {} with information {}", id, lossUpdateDto);

        service.update(id, lossUpdateDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") UUID id) throws Step1NotFoundException {
        log.info("Request received to loss with id {}", id);

        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
