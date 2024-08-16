package microstamp.step2.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import microstamp.step2.dto.state.StateReadDto;
import microstamp.step2.dto.state.StateUpdateDto;
import microstamp.step2.dto.state.StateInsertDto;
import microstamp.step2.exception.Step2NotFoundException;
import microstamp.step2.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/states")
@Tag(name = "State")
public class StateController {

    @Autowired
    private StateService stateService;

    @GetMapping
    public ResponseEntity<List<StateReadDto>> findAll() {
        return new ResponseEntity<>(stateService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<StateReadDto> findById(@PathVariable UUID id) throws Step2NotFoundException {
        return new ResponseEntity<>(stateService.findById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<StateReadDto> insert(@Valid @RequestBody StateInsertDto stateInsertDto) throws Step2NotFoundException {
        return new ResponseEntity<>(stateService.insert(stateInsertDto), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") UUID id, @Valid @RequestBody StateUpdateDto stateUpdateDto) throws Step2NotFoundException {
        stateService.update(id, stateUpdateDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<Void> delete(@PathVariable UUID id) throws Step2NotFoundException {
        stateService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
