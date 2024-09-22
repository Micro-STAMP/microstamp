package step3.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import step3.dto.export.ExportReadDto;
import step3.service.ExportService;

import java.io.IOException;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/export")
@Tag(name = "Export")
public class ExportController {
    private final ExportService exportService;

    @GetMapping("/analysis/{analysisId}/json")
    public ResponseEntity<ExportReadDto> exportToJson(@PathVariable UUID analysisId) {
        var exportReadDto = exportService.exportToJson(analysisId);
        return ResponseEntity.ok(exportReadDto);
    }

    @GetMapping("/analysis/{analysisId}/pdf")
    public ResponseEntity<byte[]> exportToPdf(@PathVariable UUID analysisId) throws IOException {

        byte[] pdf = exportService.exportToPdf(analysisId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "step3-analysis-" + analysisId + ".pdf");

        return ResponseEntity.ok().headers(headers).body(pdf);
    }
}
