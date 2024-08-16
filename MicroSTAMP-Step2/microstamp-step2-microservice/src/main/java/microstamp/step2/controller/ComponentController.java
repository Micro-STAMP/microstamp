package microstamp.step2.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import microstamp.step2.dto.component.*;
import microstamp.step2.exception.Step2NotFoundException;
import microstamp.step2.service.ComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/components")
@Tag(name = "Component")
public class ComponentController {

    @Autowired
    private ComponentService componentService;

    @GetMapping
    public ResponseEntity<List<ComponentReadDto>> findAll() {
        return new ResponseEntity<>(componentService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<ComponentReadDto> findById(@PathVariable UUID id) throws Step2NotFoundException {
        return new ResponseEntity<>(componentService.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = {"/analysis/{id}"})
    public ResponseEntity<List<ComponentReadDto>> findByAnalysisId(@PathVariable UUID id) {
        return new ResponseEntity<>(componentService.findByAnalysisId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ComponentReadDto> insert(@Valid @RequestBody ComponentInsertDto componentInsertDto) throws Step2NotFoundException {
        return new ResponseEntity<>(componentService.insert(componentInsertDto), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") UUID id, @Valid @RequestBody ComponentUpdateDto componentUpdateDto) throws Step2NotFoundException {
        componentService.update(id, componentUpdateDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        componentService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = {"/{id}/children"})
    public ResponseEntity<List<ComponentReadDto>> getComponentChildren(@PathVariable UUID id) throws Step2NotFoundException {
        List<ComponentReadDto> list = componentService.getComponentChildren(id);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(path = {"/{id}/dependencies"})
    public ResponseEntity<ComponentDependenciesDto> getComponentDependencies(@PathVariable UUID id) throws Step2NotFoundException {
        ComponentDependenciesDto list = componentService.getComponentDependencies(id);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
