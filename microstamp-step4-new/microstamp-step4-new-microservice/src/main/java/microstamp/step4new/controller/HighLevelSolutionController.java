package microstamp.step4new.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.step4new.dto.highlevelsolution.HighLevelSolutionInsertDto;
import microstamp.step4new.dto.highlevelsolution.HighLevelSolutionReadDto;
import microstamp.step4new.dto.highlevelsolution.HighLevelSolutionUpdateDto;
import microstamp.step4new.service.HighLevelSolutionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.List;

@Log4j2
@RestController
@AllArgsConstructor
@Tag(name = "HighLevelSolution")
@RequestMapping("/high-level-solutions")
public class HighLevelSolutionController {

    private final HighLevelSolutionService service;

    @GetMapping("/{id}")
    public ResponseEntity<HighLevelSolutionReadDto> findById(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @GetMapping("/formal-scenario-class/{id}")
    public ResponseEntity<HighLevelSolutionReadDto> findByFormalScenarioClassId(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(service.findByFormalScenarioClassId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HighLevelSolutionReadDto> create(@Valid @RequestBody HighLevelSolutionInsertDto dto) {
        return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") UUID id, @Valid @RequestBody HighLevelSolutionUpdateDto dto) {
        service.update(id, dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/unsafe-control-action/{id}")
    public ResponseEntity<List<HighLevelSolutionReadDto>> findByUnsafeControlActionId(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(service.findByUnsafeControlActionId(id), HttpStatus.OK);
    }
}