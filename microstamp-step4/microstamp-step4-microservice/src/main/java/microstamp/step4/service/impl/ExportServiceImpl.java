package microstamp.step4.service.impl;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.WebColors;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.step4.dto.export.ExportReadDto;
import microstamp.step4.service.ExportService;
import microstamp.step4.service.FourTupleService;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@Log4j2
@Component
@AllArgsConstructor
public class ExportServiceImpl implements ExportService {

    private final FourTupleService fourTupleService;

    public ExportReadDto exportToJson(UUID analysisId) {
        log.info("Exporting (JSON) Step 4 content of an analysis by its UUID: {}", analysisId);
        return getExportDto(analysisId);
    }

    public byte[] exportToPdf(UUID analysisId) throws IOException {
        log.info("Exporting (PDF) Step 4 content of an analysis by its UUID: {}", analysisId);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter pdfWriter = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);

        ExportReadDto exportReadDto = getExportDto(analysisId);

        setTitle(document);
        setAnalysisSection(document, exportReadDto.getAnalysisId());
        //setFourTuplesSection(document, exportReadDto.getFourTuples()); TODO
        //setUnsafeControlActionsSection(document, exportReadDto.getUnsafeControlActions());

        document.close();

        return byteArrayOutputStream.toByteArray();
    }

    private ExportReadDto getExportDto(UUID analysisId) {
        return ExportReadDto.builder()
                .analysisId(analysisId)
                .fourTuples(fourTupleService.findByAnalysisId(analysisId))
                .unsafeControlActions(fourTupleService.findByAnalysisIdSortedByUnsafeControlActions(analysisId))
                .build();
    }

    private void setTitle(Document document) throws IOException {
        Style style = new Style();
        style.setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ITALIC));

        document.add(new Paragraph("MICROSTAMP ANALYSIS - STEP 4")
                .setFontSize(20)
                .setBold()
                .setFontColor(WebColors.getRGBColor("#b4894d"))
                .setTextAlignment(TextAlignment.CENTER)
                .addStyle(style));
    }

    private void setAnalysisSection(Document document, UUID analysisId) {
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Analysis id: " + analysisId.toString()));
    }
}
