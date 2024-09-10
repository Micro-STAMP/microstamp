package microstamp.authorization.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.authorization.dto.ExportReadDto;
import microstamp.authorization.service.ExportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.UUID;

@Log4j2
@RestController
@AllArgsConstructor
@Tag(name = "Export")
@RequestMapping("/export")
public class ExportController {

    private final ExportService service;

    @GetMapping("/analysis/{id}/json")
    public ResponseEntity<ExportReadDto> exportToJson(@PathVariable(name = "id") UUID analysisId) {
        log.info("Request received to export an analysis to JSON by its id {}", analysisId);
        return new ResponseEntity<>(service.exportToJson(analysisId), HttpStatus.OK);
    }

    @GetMapping("/analysis/{id}/pdf")
    public ResponseEntity<byte[]> exportToPdf(@PathVariable(name = "id") UUID analysisId) throws IOException {
        log.info("Request received to export an analysis to PDF by its id {}", analysisId);

        byte[] pdf = service.exportToPdf(analysisId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "microstamp-analysis-" + analysisId + ".pdf");

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}
