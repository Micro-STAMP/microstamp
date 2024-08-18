package microstamp.step2.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import microstamp.step2.dto.component.ComponentBaseInsertDto;
import microstamp.step2.dto.component.ComponentReadDto;
import microstamp.step2.dto.component.ComponentUpdateDto;
import microstamp.step2.enumeration.ComponentType;
import microstamp.step2.exception.Step2NotFoundException;
import microstamp.step2.service.ComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/sensors")
@Tag(name = "Sensor")
public class SensorController {

    @Autowired
    private ComponentService componentService;

    @GetMapping
    public ResponseEntity<List<ComponentReadDto>> findAll() {
        return new ResponseEntity<>(componentService.findAll(ComponentType.SENSOR), HttpStatus.OK);
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<ComponentReadDto> findById(@PathVariable UUID id) throws Step2NotFoundException {
        return new ResponseEntity<>(componentService.findById(id, ComponentType.SENSOR), HttpStatus.OK);
    }

    @GetMapping(path = {"/analysis/{id}"})
    public ResponseEntity<List<ComponentReadDto>> findByAnalysisId(@PathVariable UUID id) {
        return new ResponseEntity<>(componentService.findByAnalysisId(id, ComponentType.SENSOR), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ComponentReadDto> insert(@Valid @RequestBody ComponentBaseInsertDto componentBaseInsertDto) throws Step2NotFoundException {
        return new ResponseEntity<>(componentService.insert(componentBaseInsertDto, ComponentType.SENSOR), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") UUID id, @Valid @RequestBody ComponentUpdateDto componentUpdateDto) throws Step2NotFoundException {
        componentService.update(id, componentUpdateDto, ComponentType.SENSOR);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<Void> delete(@PathVariable UUID id) throws Step2NotFoundException {
        componentService.delete(id, ComponentType.SENSOR);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}