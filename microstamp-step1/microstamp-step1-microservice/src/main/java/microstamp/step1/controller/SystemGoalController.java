package microstamp.step1.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.step1.dto.systemgoal.SystemGoalInsertDto;
import microstamp.step1.dto.systemgoal.SystemGoalReadDto;
import microstamp.step1.dto.systemgoal.SystemGoalUpdateDto;
import microstamp.step1.exception.Step1NotFoundException;
import microstamp.step1.service.SystemGoalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Log4j2
@RestController
@AllArgsConstructor
@Tag(name = "SystemGoal")
@RequestMapping("/system-goals")
public class SystemGoalController {

    private SystemGoalService service;

    @GetMapping
    public ResponseEntity<List<SystemGoalReadDto>> findAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SystemGoalReadDto> findById(@PathVariable(name = "id") UUID id) throws Step1NotFoundException {
        log.info("Request received to find system goal by id {}", id);

        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @GetMapping("/analysis/{id}")
    public ResponseEntity<List<SystemGoalReadDto>> findByAnalysisId(@PathVariable(name = "id") UUID id) {
        log.info("Request received to find system goal by analysis id {}", id);

        return new ResponseEntity<>(service.findByAnalysisId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SystemGoalReadDto> insert(@Valid @RequestBody SystemGoalInsertDto systemGoalInsertDto) throws Step1NotFoundException {
        log.info("Request received to insert system goal with information {}", systemGoalInsertDto);

        return new ResponseEntity<>(service.insert(systemGoalInsertDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable(name = "id") UUID id, @Valid @RequestBody SystemGoalUpdateDto systemGoalUpdateDto) throws Step1NotFoundException {
        log.info("Request received to update system goal with id {} with information {}", id, systemGoalUpdateDto);

        service.update(id, systemGoalUpdateDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") UUID id) throws Step1NotFoundException {
        log.info("Request received to delete system goal with id {}", id);

        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
