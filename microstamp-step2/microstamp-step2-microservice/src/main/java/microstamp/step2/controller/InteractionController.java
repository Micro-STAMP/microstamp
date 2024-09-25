package microstamp.step2.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import microstamp.step2.dto.interaction.InteractionReadDto;
import microstamp.step2.dto.interaction.InteractionUpdateDto;
import microstamp.step2.dto.interaction.InteractionInsertDto;
import microstamp.step2.exception.Step2NotFoundException;
import microstamp.step2.service.InteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/interactions")
@Tag(name = "Interaction")
public class InteractionController {

    @Autowired
    private InteractionService interactionService;

    @GetMapping
    public ResponseEntity<List<InteractionReadDto>> findAll() {
        return new ResponseEntity<>(interactionService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<InteractionReadDto> findById(@PathVariable("id") UUID id) throws Step2NotFoundException {
        return new ResponseEntity<>(interactionService.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = {"/connection/{id}"})
    public ResponseEntity<List<InteractionReadDto>> findByConnectionId(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(interactionService.findByConnectionId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<InteractionReadDto> insert(@Valid @RequestBody InteractionInsertDto interactionInsertDto) throws Step2NotFoundException {
        return new ResponseEntity<>(interactionService.insert(interactionInsertDto), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") UUID id, @Valid @RequestBody InteractionUpdateDto interactionUpdateDto) throws Step2NotFoundException {
        interactionService.update(id, interactionUpdateDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) throws Step2NotFoundException {
        interactionService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
