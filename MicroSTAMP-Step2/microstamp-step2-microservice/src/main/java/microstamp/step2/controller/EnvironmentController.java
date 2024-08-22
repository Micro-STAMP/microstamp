package microstamp.step2.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import microstamp.step2.dto.environment.EnvironmentReadDto;
import microstamp.step2.exception.Step2NotFoundException;
import microstamp.step2.service.EnvironmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/environments")
@Tag(name = "Environment")
public class EnvironmentController {

    @Autowired
    private EnvironmentService environmentService;

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<EnvironmentReadDto> findById(@PathVariable("id") UUID id) throws Step2NotFoundException {
        return new ResponseEntity<>(environmentService.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = {"/analysis/{id}"})
    public ResponseEntity<EnvironmentReadDto> findByAnalysisId(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(environmentService.findByAnalysisId(id), HttpStatus.OK);
    }

    @PostMapping(value = "/analysis/{id}")
    public ResponseEntity<EnvironmentReadDto> insert(@PathVariable("id") UUID analysisId) throws Step2NotFoundException {
        return new ResponseEntity<>(environmentService.insert(analysisId), HttpStatus.CREATED);
    }

    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) throws Step2NotFoundException {
        environmentService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
