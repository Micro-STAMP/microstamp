package microstamp.cast.step5.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import microstamp.cast.step5.dto.recommendation.RecommendationInsertDto;
import microstamp.cast.step5.dto.recommendation.RecommendationReadDto;
import microstamp.cast.step5.dto.recommendation.RecommendationUpdateDto;
import microstamp.cast.step5.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Tag(name = "CAST - Reccomendations")
@RequestMapping("/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService service;

    @PostMapping
    public ResponseEntity<RecommendationReadDto> create(
            @Valid @RequestBody RecommendationInsertDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/analysis/{analysisId}")
    public ResponseEntity<List<RecommendationReadDto>> findByAnalysisId(
            @PathVariable UUID analysisId) {
        return ResponseEntity.ok(service.findByAnalysisId(analysisId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecommendationReadDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecommendationReadDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody RecommendationUpdateDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
