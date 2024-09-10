package microstamp.step1.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.WebColors;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.step1.dto.assumption.AssumptionReadDto;
import microstamp.step1.dto.export.ExportDto;
import microstamp.step1.dto.hazard.HazardReadDto;
import microstamp.step1.dto.loss.LossReadDto;
import microstamp.step1.dto.systemgoal.SystemGoalReadDto;
import microstamp.step1.dto.systemsafetyconstraint.SystemSafetyConstraintReadDto;
import microstamp.step1.service.*;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Log4j2
@Component
@AllArgsConstructor
public class ExportServiceImpl implements ExportService {

    private final SystemGoalService systemGoalService;

    private final AssumptionService assumptionService;

    private final LossService lossService;

    private final HazardService hazardService;

    private final SystemSafetyConstraintService systemSafetyConstraintService;

    public ExportDto exportToJson(UUID analysisId) {
        log.info("Exporting (JSON) Step 1 content of an analysis by its UUID: {}", analysisId);
        return getExportDto(analysisId);
    }

    public byte[] exportToPdf(UUID analysisId) throws IOException {
        log.info("Exporting (PDF) Step 1 content of an analysis by its UUID: {}", analysisId);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter pdfWriter = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);

        ExportDto exportDto = getExportDto(analysisId);

        setTitle(document);
        setSystemGoalSection(document, exportDto.getSystemGoals());
        setAssumptionSection(document, exportDto.getAssumptions());
        setLossesSection(document, exportDto.getLosses());
        setHazardsSection(document, exportDto.getHazards());
        setSystemSafetyConstraintSection(document, exportDto.getSystemSafetyConstraints());

        document.close();

        return byteArrayOutputStream.toByteArray();
    }

    private ExportDto getExportDto(UUID analysisId) {
        return ExportDto.builder()
                .analysisId(analysisId)
                .systemGoals(systemGoalService.findByAnalysisId(analysisId))
                .assumptions(assumptionService.findByAnalysisId(analysisId))
                .losses(lossService.findByAnalysisId(analysisId))
                .hazards(hazardService.findByAnalysisId(analysisId))
                .systemSafetyConstraints(systemSafetyConstraintService.findByAnalysisId(analysisId))
                .build();
    }

    private void setTitle(Document document) throws IOException {
        Style style = new Style();
        style.setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ITALIC));

        document.add(new Paragraph("MICROSTAMP ANALYSIS - STEP 1")
                .setFontSize(20)
                .setBold()
                .setFontColor(WebColors.getRGBColor("#b4894d"))
                .setTextAlignment(TextAlignment.CENTER)
                .addStyle(style));
    }

    private void setSystemGoalSection(Document document, List<SystemGoalReadDto> systemGoals) throws JsonProcessingException {
        com.itextpdf.layout.element.List systemGoalsList = new com.itextpdf.layout.element.List();

        for(SystemGoalReadDto systemGoal : systemGoals)
            systemGoalsList.add("[" + systemGoal.getCode() + "] " + systemGoal.getName());

        document.add(new Paragraph("\n"));
        document.add(new Paragraph("System Goals")
                .setBold()
                .setUnderline());
        document.add(systemGoalsList);
    }

    private void setAssumptionSection(Document document, List<AssumptionReadDto> assumptions) throws JsonProcessingException {
        com.itextpdf.layout.element.List assumptionsList = new com.itextpdf.layout.element.List();

        for(AssumptionReadDto assumption : assumptions)
            assumptionsList.add("[" + assumption.getCode() + "] " + assumption.getName());

        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Assumptions")
                .setBold()
                .setUnderline());
        document.add(assumptionsList);
    }

    private void setLossesSection(Document document, List<LossReadDto> losses) throws JsonProcessingException {
        com.itextpdf.layout.element.List lossesList = new com.itextpdf.layout.element.List();

        for(LossReadDto loss : losses)
            lossesList.add("[" + loss.getCode() + "] " + loss.getName());

        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Losses")
                .setBold()
                .setUnderline());
        document.add(lossesList);
    }

    private void setHazardsSection(Document document, List<HazardReadDto> hazards) throws JsonProcessingException {
        com.itextpdf.layout.element.List hazardsList = new com.itextpdf.layout.element.List();

        for(HazardReadDto hazard : hazards) {
            StringBuilder hazardRow = new StringBuilder("[" + hazard.getCode() + "] " + hazard.getName());
            if(hazard.getFather() != null)
                hazardRow.append(" [").append(hazard.getFather().getCode()).append("] ");

            for(LossReadDto loss : hazard.getLosses())
                hazardRow.append(" [").append(loss.getCode()).append("] ");

            hazardsList.add(hazardRow.toString());
        }

        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Hazards")
                .setBold()
                .setUnderline());
        document.add(hazardsList);
    }

    private void setSystemSafetyConstraintSection(Document document, List<SystemSafetyConstraintReadDto> systemSafetyConstraints) throws JsonProcessingException {
        com.itextpdf.layout.element.List systemSafetyConstraintsList = new com.itextpdf.layout.element.List();

        for(SystemSafetyConstraintReadDto systemSafetyConstraint : systemSafetyConstraints) {
            StringBuilder systemSafetyConstraintRow = new StringBuilder("[" + systemSafetyConstraint.getCode() + "] " + systemSafetyConstraint.getName());

            for(HazardReadDto hazard : systemSafetyConstraint.getHazards())
                systemSafetyConstraintRow.append(" [").append(hazard.getCode()).append("] ");

            systemSafetyConstraintsList.add(systemSafetyConstraintRow.toString());
        }

        document.add(new Paragraph("\n"));
        document.add(new Paragraph("System Safety Constraints")
                .setBold()
                .setUnderline());
        document.add(systemSafetyConstraintsList);
    }
}
