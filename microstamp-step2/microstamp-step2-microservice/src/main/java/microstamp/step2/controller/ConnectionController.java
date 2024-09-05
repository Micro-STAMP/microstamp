package microstamp.step2.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import microstamp.step2.dto.connection.ConnectionReadDto;
import microstamp.step2.dto.connection.ConnectionUpdateDto;
import microstamp.step2.dto.connection.ConnectionInsertDto;
import microstamp.step2.exception.Step2NotFoundException;
import microstamp.step2.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/connections")
@Tag(name = "Connection")
public class ConnectionController {

    @Autowired
    private ConnectionService connectionService;

    @GetMapping
    public ResponseEntity<List<ConnectionReadDto>> findAll() {
        return new ResponseEntity<>(connectionService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<ConnectionReadDto> findById(@PathVariable("id") UUID id) throws Step2NotFoundException {
        return new ResponseEntity<>(connectionService.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = {"/analysis/{id}"})
    public ResponseEntity<List<ConnectionReadDto>> findByAnalysisId(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(connectionService.findByAnalysisId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ConnectionReadDto> insert(@Valid @RequestBody ConnectionInsertDto connectionInsertDto) throws Step2NotFoundException {
        return new ResponseEntity<>(connectionService.insert(connectionInsertDto), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") UUID id, @Valid @RequestBody ConnectionUpdateDto connectionUpdateDto) throws Step2NotFoundException {
        connectionService.update(id, connectionUpdateDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) throws Step2NotFoundException {
        connectionService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
