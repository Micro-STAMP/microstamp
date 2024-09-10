package microstamp.authorization.util.pdf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import microstamp.authorization.dto.step1.*;

import java.util.List;
import java.util.UUID;

public class Step1PdfHelper {

    public static void setAnalysisSection(Document document, UUID analysisId) {
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Analysis id: " + analysisId.toString()));
    }

    public static void setSystemGoalSection(Document document, List<SystemGoalReadDto> systemGoals) throws JsonProcessingException {
        com.itextpdf.layout.element.List systemGoalsList = new com.itextpdf.layout.element.List();

        for(SystemGoalReadDto systemGoal : systemGoals)
            systemGoalsList.add("[" + systemGoal.getCode() + "] " + systemGoal.getName());

        document.add(new Paragraph("System Goals")
                .setBold()
                .setUnderline());
        document.add(systemGoalsList);
    }

    public static void setAssumptionSection(Document document, List<AssumptionReadDto> assumptions) throws JsonProcessingException {
        com.itextpdf.layout.element.List assumptionsList = new com.itextpdf.layout.element.List();

        for(AssumptionReadDto assumption : assumptions)
            assumptionsList.add("[" + assumption.getCode() + "] " + assumption.getName());

        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Assumptions")
                .setBold()
                .setUnderline());
        document.add(assumptionsList);
    }

    public static void setLossesSection(Document document, List<LossReadDto> losses) throws JsonProcessingException {
        com.itextpdf.layout.element.List lossesList = new com.itextpdf.layout.element.List();

        for(LossReadDto loss : losses)
            lossesList.add("[" + loss.getCode() + "] " + loss.getName());

        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Losses")
                .setBold()
                .setUnderline());
        document.add(lossesList);
    }

    public static void setHazardsSection(Document document, List<HazardReadDto> hazards) throws JsonProcessingException {
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

    public static void setSystemSafetyConstraintSection(Document document, List<SystemSafetyConstraintReadDto> systemSafetyConstraints) throws JsonProcessingException {
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
