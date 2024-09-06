package microstamp.step2.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import microstamp.step2.dto.variable.VariableReadDto;
import microstamp.step2.dto.variable.VariableUpdateDto;
import microstamp.step2.dto.variable.VariableInsertDto;
import microstamp.step2.exception.Step2NotFoundException;
import microstamp.step2.service.VariableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/variables")
@Tag(name = "Variable")
public class VariableController {

    @Autowired
    private VariableService variableService;

    @GetMapping
    public ResponseEntity<List<VariableReadDto>> findAll() {
        return new ResponseEntity<>(variableService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<VariableReadDto> findById(@PathVariable("id") UUID id) throws Step2NotFoundException {
        return new ResponseEntity<>(variableService.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = {"/analysis/{id}"})
    public ResponseEntity<List<VariableReadDto>> findByAnalysisId(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(variableService.findByAnalysisId(id), HttpStatus.OK);
    }

    @GetMapping(path = {"/component/{id}"})
    public ResponseEntity<List<VariableReadDto>> findByComponentId(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(variableService.findByComponentId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<VariableReadDto> insert(@Valid @RequestBody VariableInsertDto variableInsertDto) throws Step2NotFoundException {
        return new ResponseEntity<>(variableService.insert(variableInsertDto), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") UUID id, @Valid @RequestBody VariableUpdateDto variableUpdateDto) throws Step2NotFoundException {
        variableService.update(id, variableUpdateDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) throws Step2NotFoundException {
        variableService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
