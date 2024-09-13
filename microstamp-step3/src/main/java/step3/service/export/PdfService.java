package step3.service.export;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.WebColors;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import step3.dto.rule.RuleReadListDto;
import step3.dto.step2.StateReadDto;
import step3.dto.unsafe_control_action.UnsafeControlActionReadDto;
import step3.service.RuleService;
import step3.service.SafetyConstraintService;
import step3.service.UnsafeControlActionService;

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


        generateRuleContent(document, analysisId);
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

        // Define table with 2 columns
        Table table = new Table(UnitValue.createPercentArray(new float[]{50, 50}))
                .useAllAvailableWidth();

        // Adding header
        table.addHeaderCell("Unsafe Control Action");
        table.addHeaderCell("Safety Constraint");

        // Populating table rows
        for (UnsafeControlActionReadDto uca : ucaList) {
            StringBuilder ucaName = new StringBuilder(uca.name());
            ucaName.append(" [").append(uca.hazard_code()).append("]");
            if (!uca.rule_code().isEmpty()) {
                ucaName.append("[").append(uca.rule_code()).append("]");
            }

            table.addCell(ucaName.toString());
            table.addCell(safetyConstraintService.readSafetyConstraintByUCAId(uca.id()).name());
        }

        document.add(table);
        document.add(new Paragraph("\n"));
    }

    private void generateRuleContent(Document document, UUID analysisId) {
        List<RuleReadListDto> ruleList = ruleService.readRulesByAnalysisId(analysisId);

        // Define table with 6 columns
        Table table = new Table(UnitValue.createPercentArray(new float[]{10, 15, 15, 15, 15, 15}))
                .useAllAvailableWidth();

        // Adding header
        table.addHeaderCell("Rule");
        table.addHeaderCell("Control Action Name");
        table.addHeaderCell("States");
        table.addHeaderCell("Types");
        table.addHeaderCell("Hazard");
        table.addHeaderCell("Code");

        // Show Rule's states as a list of strings
        List<String> statesNames = ruleList.get(0)
                .states()
                .stream()
                .map(StateReadDto::name)
                .toList();

        // Populating table rows
        for (RuleReadListDto rule : ruleList) {
            table.addCell(rule.name());
            table.addCell(rule.control_action_name());
            table.addCell(String.join(", ", statesNames));
            table.addCell(rule.types().toString());
            table.addCell(rule.hazard().name());
            table.addCell(rule.code());
        }

        document.add(table);
        document.add(new Paragraph("\n"));
    }
}
