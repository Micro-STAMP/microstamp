package microstamp.step4.service.impl;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.WebColors;
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
import lombok.extern.log4j.Log4j2;
import microstamp.step4.dto.export.ExportReadDto;
import microstamp.step4.dto.fourtuple.FourTupleFullReadDto;
import microstamp.step4.dto.fourtuple.FourTupleReadDto;
import microstamp.step4.dto.unsafecontrolaction.UnsafeControlActionFullReadDto;
import microstamp.step4.service.ExportService;
import microstamp.step4.service.FourTupleService;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@Component
@AllArgsConstructor
public class ExportServiceImpl implements ExportService {

    private final FourTupleService fourTupleService;

    private static final float[] FOUR_TUPLE_COLUMN_WIDTHS = new float[]{8f, 71f, 21f};

    private static final float[] UCA_COLUMN_WIDTHS = new float[]{8f, 23f, 23f, 23f, 23f};

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
        setFourTuplesSection(document, exportReadDto.getFourTuples());
        setUnsafeControlActionsSection(document, exportReadDto.getUnsafeControlActions());

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

    private void setFourTuplesSection(Document document, List<FourTupleFullReadDto> fourTuples) {
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("4-Tuples")
                .setBold()
                .setUnderline());

        if (fourTuples == null || fourTuples.isEmpty())
            return;

        Table table = new Table(UnitValue.createPercentArray(FOUR_TUPLE_COLUMN_WIDTHS))
                .useAllAvailableWidth();

        table.addHeaderCell("Code");
        table.addHeaderCell("4-Tuple");
        table.addHeaderCell("Unsafe Control Actions");

        for (FourTupleFullReadDto fourTuple : fourTuples) {
            String description = String.format(
                    "[Scenario] %s\n\n[Associated Causal Factor] %s\n\n[Recommendation] %s\n\n[Rationale] %s",
                    fourTuple.getScenario(),
                    fourTuple.getAssociatedCausalFactor(),
                    fourTuple.getRecommendation(),
                    fourTuple.getRationale()
            );

            String ucaCodes = fourTuple.getUnsafeControlActions() == null || fourTuple.getUnsafeControlActions().isEmpty()
                    ? ""
                    : fourTuple.getUnsafeControlActions().stream()
                    .map(uca -> "[" + uca.getUca_code() + "]")
                    .collect(Collectors.joining(" "));

            table.addCell(fourTuple.getCode());
            table.addCell(description);
            table.addCell(ucaCodes);
        }

        document.add(table);
    }

    private void setUnsafeControlActionsSection(Document document, List<UnsafeControlActionFullReadDto> unsafeControlActions) {
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Unsafe Control Actions")
                .setBold()
                .setUnderline());

        if (unsafeControlActions == null || unsafeControlActions.isEmpty())
            return;

        for(UnsafeControlActionFullReadDto unsafeControlAction : unsafeControlActions) {
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("[" + unsafeControlAction.getUca_code() + "] " + unsafeControlAction.getName())
                    .setBold());

            Table table = new Table(UnitValue.createPercentArray(UCA_COLUMN_WIDTHS))
                    .useAllAvailableWidth();

            table.addHeaderCell("Code");
            table.addHeaderCell("Scenario");
            table.addHeaderCell("Associated Causal Factor");
            table.addHeaderCell("Recommendation");
            table.addHeaderCell("Rationale");

            for(FourTupleReadDto fourTuple : unsafeControlAction.getFourTuples()) {
                table.addCell(fourTuple.getCode() != null ? fourTuple.getCode() : "");
                table.addCell(fourTuple.getScenario());
                table.addCell(fourTuple.getAssociatedCausalFactor());
                table.addCell(fourTuple.getRecommendation());
                table.addCell(fourTuple.getRationale());
            }

            document.add(table);
        }
    }
}
