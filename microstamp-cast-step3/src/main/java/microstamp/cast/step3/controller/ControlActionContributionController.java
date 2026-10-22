package microstamp.cast.step3.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import microstamp.cast.step3.dto.controlactioncontribution.ControlActionContributionInsertDto;
import microstamp.cast.step3.dto.controlactioncontribution.ControlActionContributionReadDto;
import microstamp.cast.step3.dto.controlactioncontribution.ControlActionContributionUpdateDto;
import microstamp.cast.step3.service.ControlActionContributionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Tag(name = "CAST - Control Action Contributions")
@RequestMapping("/control-action-contributions")
@RequiredArgsConstructor
public class ControlActionContributionController {

    private final ControlActionContributionService service;

    @PostMapping
    public ResponseEntity<ControlActionContributionReadDto> create(
            @Valid @RequestBody ControlActionContributionInsertDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/analysis/{analysisId}")
    public ResponseEntity<List<ControlActionContributionReadDto>> findByAnalysisId(
            @PathVariable UUID analysisId) {
        return ResponseEntity.ok(service.findByAnalysisId(analysisId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ControlActionContributionReadDto> findById(
            @PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ControlActionContributionReadDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody ControlActionContributionUpdateDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
