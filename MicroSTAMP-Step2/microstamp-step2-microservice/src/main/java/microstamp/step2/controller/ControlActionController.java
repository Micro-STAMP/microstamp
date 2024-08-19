package microstamp.step2.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import microstamp.step2.dto.controlaction.ControlActionReadDto;
import microstamp.step2.service.ControlActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/control-actions")
@Tag(name = "ControlAction")
public class ControlActionController {

    @Autowired
    private ControlActionService controlActionService;

    @GetMapping
    public ResponseEntity<List<ControlActionReadDto>> findAll() {
        return new ResponseEntity<>(controlActionService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<ControlActionReadDto> findById(@PathVariable UUID id) {
        return new ResponseEntity<>(controlActionService.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = {"/component/{id}"})
    public ResponseEntity<List<ControlActionReadDto>> findByComponentId(@PathVariable UUID id) {
        return new ResponseEntity<>(controlActionService.findByComponentId(id), HttpStatus.OK);
    }

    @GetMapping(path = {"/connection/{id}"})
    public ResponseEntity<List<ControlActionReadDto>> findByConnectionId(@PathVariable UUID id) {
        return new ResponseEntity<>(controlActionService.findByConnectionId(id), HttpStatus.OK);
    }
}
