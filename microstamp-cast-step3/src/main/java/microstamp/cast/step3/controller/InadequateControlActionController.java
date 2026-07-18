package microstamp.cast.step3.controller;

import lombok.RequiredArgsConstructor;
import microstamp.cast.step3.dto.inadequatecontrolaction.InadequateControlActionInsertDto;
import microstamp.cast.step3.dto.inadequatecontrolaction.InadequateControlActionReadDto;
import microstamp.cast.step3.dto.inadequatecontrolaction.InadequateControlActionUpdateDto;
import microstamp.cast.step3.service.InadequateControlActionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/inadequate-control-actions")
@RequiredArgsConstructor
public class InadequateControlActionController {

    private final InadequateControlActionService service;

    @PostMapping
    public ResponseEntity<InadequateControlActionReadDto> create(@RequestBody InadequateControlActionInsertDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/analysis/{analysisId}")
    public ResponseEntity<List<InadequateControlActionReadDto>> findByAnalysisId(@PathVariable UUID analysisId) {
        return ResponseEntity.ok(service.findByAnalysisId(analysisId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InadequateControlActionReadDto> update(@PathVariable UUID id, @RequestBody InadequateControlActionUpdateDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}