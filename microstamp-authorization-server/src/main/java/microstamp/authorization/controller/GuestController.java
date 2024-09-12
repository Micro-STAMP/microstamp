package microstamp.authorization.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.authorization.dto.AnalysisReadDto;
import microstamp.authorization.dto.ExportReadDto;
import microstamp.authorization.service.GuestService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Log4j2
@RestController
@AllArgsConstructor
@Tag(name = "Guest")
@RequestMapping("/guests")
public class GuestController {

    private final GuestService guestService;

    @GetMapping(path = {"analyses"})
    public ResponseEntity<List<AnalysisReadDto>> findAll() {
        return new ResponseEntity<>(guestService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = {"analyses/{id}"})
    public ResponseEntity<AnalysisReadDto> findById(@PathVariable(name = "id") UUID analysisId) {
        log.info("Request received to find guest analysis by id {}", analysisId);
        return new ResponseEntity<>(guestService.findById(analysisId), HttpStatus.OK);
    }

    @GetMapping(path = {"analyses/export/json"})
    public ResponseEntity<List<ExportReadDto>> exportAll() {
        log.info("Request received to find guest analyses");
        return new ResponseEntity<>(guestService.exportAll(), HttpStatus.OK);
    }

    @GetMapping(path = {"analyses/{id}/export/json"})
    public ResponseEntity<ExportReadDto> exportToJson(@PathVariable("id") UUID analysisId) {
        log.info("Request received to export a guest analysis to JSON by its id {}", analysisId);
        return new ResponseEntity<>(guestService.exportToJson(analysisId), HttpStatus.OK);
    }

    @GetMapping("/analyses/{id}/export/pdf")
    public ResponseEntity<byte[]> exportToPdf(@PathVariable(name = "id") UUID analysisId) throws IOException {
        log.info("Request received to export a guest analysis to PDF by its id {}", analysisId);

        byte[] pdf = guestService.exportToPdf(analysisId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "microstamp-analysis-" + analysisId + ".pdf");

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}
