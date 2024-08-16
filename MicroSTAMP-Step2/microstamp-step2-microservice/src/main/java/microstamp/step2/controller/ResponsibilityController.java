package microstamp.step2.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import microstamp.step2.dto.responsibility.ResponsibilityReadDto;
import microstamp.step2.dto.responsibility.ResponsibilityUpdateDto;
import microstamp.step2.dto.responsibility.ResponsibilityInsertDto;
import microstamp.step2.exception.Step2NotFoundException;
import microstamp.step2.service.ResponsibilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/responsibilities")
@Tag(name = "Responsibility")
public class ResponsibilityController {

    @Autowired
    private ResponsibilityService responsibilityService;

    @GetMapping
    public ResponseEntity<List<ResponsibilityReadDto>> findAll() {
        return new ResponseEntity<>(responsibilityService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<ResponsibilityReadDto> findById(@PathVariable UUID id) throws Step2NotFoundException {
        return new ResponseEntity<>(responsibilityService.findById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponsibilityReadDto> insert(@Valid @RequestBody ResponsibilityInsertDto responsibilityInsertDto) throws Exception {
        return new ResponseEntity<>(responsibilityService.insert(responsibilityInsertDto), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") UUID id, @Valid @RequestBody ResponsibilityUpdateDto responsibilityUpdateDto) throws Step2NotFoundException {
        responsibilityService.update(id, responsibilityUpdateDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<Void> delete(@PathVariable UUID id) throws Step2NotFoundException {
        responsibilityService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
