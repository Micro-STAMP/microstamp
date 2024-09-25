package step3.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import step3.dto.not_uca_context.NotUcaContextCreateDto;
import step3.dto.not_uca_context.NotUcaContextReadDto;
import step3.service.NotUcaContextService;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/not-uca-contexts")
@Tag(name = "NotUnsafeControlActionContext")
public class NotUcaContextController {
    private final NotUcaContextService notUcaContextService;

    @PostMapping
    @Transactional
    public ResponseEntity<NotUcaContextReadDto> createNotUcaContext(@RequestBody NotUcaContextCreateDto notUcaContextCreateDto) {
        NotUcaContextReadDto notUcaContext = notUcaContextService.createNotUcaContext(notUcaContextCreateDto);
        URI uri = URI.create("/not-uca-contexts/analyses/" + notUcaContext.analysis_id());
        return ResponseEntity.created(uri).body(notUcaContext);
    }

    @GetMapping("/analyses/{analysisId}")
    public ResponseEntity<List<NotUcaContextReadDto>> readNotUcaContext(@PathVariable UUID analysisId) {
        List<NotUcaContextReadDto> notUcaContextReadDtoList = notUcaContextService.getNotUcaContextsByAnalysisId(analysisId);
        return ResponseEntity.ok(notUcaContextReadDtoList);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteNotUcaContext(@PathVariable UUID id) {
        notUcaContextService.deleteNotUcaContext(id);
        return ResponseEntity.noContent().build();
    }
}
