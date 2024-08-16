package microstamp.authorization.controller;

import jakarta.validation.Valid;
import microstamp.authorization.dto.AnalysisInsertDto;
import microstamp.authorization.dto.AnalysisReadDto;
import microstamp.authorization.dto.AnalysisUpdateDto;
import microstamp.authorization.exception.NotFoundException;
import microstamp.authorization.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/analyses")
public class AnalysisController {

    @Autowired
    private AnalysisService analysisService;

    @GetMapping
    public ResponseEntity<List<AnalysisReadDto>> findAll() {
        return new ResponseEntity<>(analysisService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnalysisReadDto> findById(@PathVariable(name = "id") UUID id) throws NotFoundException {
        return new ResponseEntity<>(analysisService.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = {"user/{id}"})
    public ResponseEntity<List<AnalysisReadDto>> findByUserId(@PathVariable UUID id) {
        return new ResponseEntity<>(analysisService.findByUserId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AnalysisReadDto> insert(@Valid @RequestBody AnalysisInsertDto analysisInsertDto) throws NotFoundException {
        return new ResponseEntity<>(analysisService.insert(analysisInsertDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnalysisReadDto> update(@PathVariable(name = "id") UUID id, @Valid @RequestBody AnalysisUpdateDto analysisUpdateDto) throws NotFoundException {
        analysisService.update(id, analysisUpdateDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") UUID id) throws NotFoundException {
        analysisService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
