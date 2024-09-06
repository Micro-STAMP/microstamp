package step3.controller.mit.export;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import step3.service.mit.export.PdfService;

import java.io.IOException;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class PdfController {
    private final PdfService pdfService;

    @GetMapping("/analysis/{analysisId}/export/pdf")
    public ResponseEntity<byte[]> generatePdfFromAnalysisId(@PathVariable UUID analysisId) throws IOException {
        byte[] pdf = pdfService.generatePdfFromAnalysisId(analysisId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "step3-analysis-" + analysisId + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdf);
    }

}
