package microstamp.step2.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import microstamp.step2.dto.connectionaction.ConnectionActionReadDto;
import microstamp.step2.dto.connectionaction.ConnectionActionUpdateDto;
import microstamp.step2.dto.connectionaction.ConnectionActionInsertDto;
import microstamp.step2.exception.Step2NotFoundException;
import microstamp.step2.service.ConnectionActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/connection-actions")
@Tag(name = "ConnectionAction")
public class ConnectionActionController {

    @Autowired
    private ConnectionActionService connectionActionService;

    @GetMapping
    public ResponseEntity<List<ConnectionActionReadDto>> findAll() {
        return new ResponseEntity<>(connectionActionService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<ConnectionActionReadDto> findById(@PathVariable("id") UUID id) throws Step2NotFoundException {
        return new ResponseEntity<>(connectionActionService.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = {"/connection/{id}"})
    public ResponseEntity<List<ConnectionActionReadDto>> findByConnectionId(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(connectionActionService.findByConnectionId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ConnectionActionReadDto> insert(@Valid @RequestBody ConnectionActionInsertDto connectionActionInsertDto) throws Step2NotFoundException {
        return new ResponseEntity<>(connectionActionService.insert(connectionActionInsertDto), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") UUID id, @Valid @RequestBody ConnectionActionUpdateDto connectionActionUpdateDto) throws Step2NotFoundException {
        connectionActionService.update(id, connectionActionUpdateDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) throws Step2NotFoundException {
        connectionActionService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
