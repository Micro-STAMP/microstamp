package microstamp.step1.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.step1.dto.systemsafetyconstraint.SystemSafetyConstraintInsertDto;
import microstamp.step1.dto.systemsafetyconstraint.SystemSafetyConstraintReadDto;
import microstamp.step1.dto.systemsafetyconstraint.SystemSafetyConstraintUpdateDto;
import microstamp.step1.exception.Step1NotFoundException;
import microstamp.step1.service.SystemSafetyConstraintService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Log4j2
@RestController
@AllArgsConstructor
@Tag(name = "SystemSafetyConstraint")
@RequestMapping("/system-safety-constraints")
public class SystemSafetyConstraintController {

    private SystemSafetyConstraintService service;

    @GetMapping
    public ResponseEntity<List<SystemSafetyConstraintReadDto>> findAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SystemSafetyConstraintReadDto> findById(@PathVariable(name = "id") UUID id) throws Step1NotFoundException {
        log.info("Request received to find system safety constraint by id {}", id);
        
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @GetMapping("/analysis/{id}")
    public ResponseEntity<List<SystemSafetyConstraintReadDto>> findByAnalysisId(@PathVariable(name = "id") UUID id) {
        log.info("Request received to find system safety constraint by analysis id {}", id);
        
        return new ResponseEntity<>(service.findByAnalysisId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SystemSafetyConstraintReadDto> insert(@Valid @RequestBody SystemSafetyConstraintInsertDto systemSafetyConstraintInsertDto) throws Step1NotFoundException {
        log.info("Request received to insert system safety constraint with information {}", systemSafetyConstraintInsertDto);
        
        return new ResponseEntity<>(service.insert(systemSafetyConstraintInsertDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable(name = "id") UUID id, @Valid @RequestBody SystemSafetyConstraintUpdateDto systemSafetyConstraintUpdateDto) throws Step1NotFoundException {
        log.info("Request received to update system safety constraint with id {} with information {}", id, systemSafetyConstraintUpdateDto);
        
        service.update(id, systemSafetyConstraintUpdateDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") UUID id) throws Step1NotFoundException {
        log.info("Request received to delete system safety constraint with id {}", id);
        
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
