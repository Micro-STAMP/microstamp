package microstamp.cast.step5.controller;

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
@RequestMapping("/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService service;

    @PostMapping
    public ResponseEntity<RecommendationReadDto> create(@RequestBody RecommendationInsertDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/analysis/{analysisId}")
    public ResponseEntity<List<RecommendationReadDto>> findByAnalysisId(@PathVariable UUID analysisId) {
        return ResponseEntity.ok(service.findByAnalysisId(analysisId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecommendationReadDto> update(@PathVariable UUID id, @RequestBody RecommendationUpdateDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
