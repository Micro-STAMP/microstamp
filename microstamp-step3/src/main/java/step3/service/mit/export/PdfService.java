package step3.service.mit.export;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.WebColors;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import step3.dto.mit.unsafe_control_action.UnsafeControlActionReadDto;
import step3.service.mit.RuleService;
import step3.service.mit.SafetyConstraintService;
import step3.service.mit.UnsafeControlActionService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PdfService {
    private final UnsafeControlActionService ucaService;
    private final RuleService ruleService;
    private final SafetyConstraintService safetyConstraintService;

    public byte[] generatePdfFromAnalysisId(UUID analysisId) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter pdfWriter = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);

        generateTitleOfDocument(document);

        // montar o conteúdo do PDF
        // vai ter informações de UCA, regras e restrições de segurança



        generateUcaAndConstraintContent(document, analysisId);

        document.close();

        return byteArrayOutputStream.toByteArray();
    }

    private void generateTitleOfDocument(Document document) throws IOException {
        Style titleStyle = new Style();
        PdfFont font = PdfFontFactory.createFont(StandardFonts.TIMES_ITALIC);
        titleStyle.setFont(font);

        Paragraph title = new Paragraph("MICROSTAMP ANALYSIS - STEP 3")
                .setFontSize(20)
                .setBold()
                .setFontColor(WebColors.getRGBColor("#b4894d"))
                .setTextAlignment(TextAlignment.CENTER)
                .addStyle(titleStyle);

        document.add(title);
        document.add(new Paragraph("\n"));
    }

    private void generateUcaAndConstraintContent(Document document, UUID analysisId) throws IOException {
        List<UnsafeControlActionReadDto> ucaList = ucaService.readAllUCAByAnalysisId(analysisId);

        Paragraph titleUCA = new Paragraph("Unsafe control action and safety constraint:")
                .setFontSize(16)
                .setBold()
                .setTextAlignment(TextAlignment.LEFT);
        document.add(titleUCA);
        document.add(new Paragraph("\n"));

        for (UnsafeControlActionReadDto uca : ucaList) {
            Paragraph ucaName = new Paragraph("Unsafe Control Action: " + uca.name());
            document.add(ucaName);

            Paragraph constraintName = new Paragraph("Safety Constraint: " + safetyConstraintService.readSafetyConstraintByUCAId(uca.id()).name());
            document.add(constraintName);
            document.add(new Paragraph("\n"));
        }
    }

    private void generateRuleContent(Document document) {

    }
}
