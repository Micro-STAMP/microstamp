package microstamp.step4new.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import jakarta.validation.Valid;
import microstamp.step4new.dto.formalscenario.FormalScenarioInsertDto;
import microstamp.step4new.dto.formalscenario.FormalScenarioReadDto;
import microstamp.step4new.service.FormalScenarioService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.UUID;

@Log4j2
@RestController
@AllArgsConstructor
@Tag(name = "FormalScenario")
@RequestMapping("/formal-scenarios")
public class FormalScenarioController {

    private final FormalScenarioService service;

    @GetMapping("/unsafe-control-action/{id}")
    public ResponseEntity<FormalScenarioReadDto> generateAndRead(@PathVariable(name = "id") UUID id){
        return new ResponseEntity<>(service.getOrCreate(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody FormalScenarioInsertDto request) {
        service.create(request.getAnalysisId(), request.getUnsafeControlActionId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FormalScenarioReadDto> findById(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
