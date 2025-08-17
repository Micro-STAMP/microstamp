package microstamp.authorization.util.pdf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itextpdf.kernel.colors.WebColors;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.ListItem;
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

        for(SystemGoalReadDto systemGoal : systemGoals) {
            ListItem systemGoalItem = new ListItem("[" + systemGoal.getCode() + "] " + systemGoal.getName());
            systemGoalItem.setDestination(systemGoal.getCode());
            systemGoalsList.add(systemGoalItem);
        }

        document.add(new Paragraph("System Goals")
                .setBold()
                .setUnderline());
        document.add(systemGoalsList);
    }

    public static void setAssumptionSection(Document document, List<AssumptionReadDto> assumptions) throws JsonProcessingException {
        com.itextpdf.layout.element.List assumptionsList = new com.itextpdf.layout.element.List();

        for(AssumptionReadDto assumption : assumptions) {
            ListItem assumptionItem = new ListItem("[" + assumption.getCode() + "] " + assumption.getName());
            assumptionItem.setDestination(assumption.getCode());
            assumptionsList.add("[" + assumption.getCode() + "] " + assumption.getName());
        }

        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Assumptions")
                .setBold()
                .setUnderline());
        document.add(assumptionsList);
    }

    public static void setLossesSection(Document document, List<LossReadDto> losses) throws JsonProcessingException {
        com.itextpdf.layout.element.List lossesList = new com.itextpdf.layout.element.List();

        for(LossReadDto loss : losses) {
            ListItem lossItem = new ListItem("[" + loss.getCode() + "] " + loss.getName());
            lossItem.setDestination(loss.getCode());
            lossesList.add(lossItem);
        }

        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Losses")
                .setBold()
                .setUnderline());
        document.add(lossesList);
    }

    public static void setHazardsSection(Document document, List<HazardReadDto> hazards) throws JsonProcessingException {
        com.itextpdf.layout.element.List hazardsList = new com.itextpdf.layout.element.List();

        for(HazardReadDto hazard : hazards) {
            Paragraph hazardParagraph = new Paragraph("[" + hazard.getCode() + "] " + hazard.getName());
            hazardParagraph.setDestination(hazard.getCode());

            if (hazard.getFather() != null) {
                hazardParagraph.add(" [" + hazard.getFather().getCode() + "] ");
                hazardParagraph.setDestination(hazard.getFather().getCode());
            }

            for (LossReadDto loss : hazard.getLosses()) {
                Link lossLink = new Link(" [" + loss.getCode() + "] ", PdfAction.createGoTo(loss.getCode()));
                lossLink.setFontColor(WebColors.getRGBColor("#b4894d"));
                hazardParagraph.add(lossLink);
            }

            ListItem hazardItem = new ListItem();
            hazardItem.add(hazardParagraph);
            hazardsList.add(hazardItem);
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
            Paragraph systemSafetyConstraintParagraph = new Paragraph("[" + systemSafetyConstraint.getCode() + "] " + systemSafetyConstraint.getName());
            systemSafetyConstraintParagraph.setDestination(systemSafetyConstraint.getCode());

            for (HazardReadDto hazard : systemSafetyConstraint.getHazards()) {
                Link hazardLink = new Link(" [" + hazard.getCode() + "] ", PdfAction.createGoTo(hazard.getCode()));
                hazardLink.setFontColor(WebColors.getRGBColor("#b4894d"));
                systemSafetyConstraintParagraph.add(hazardLink);
            }

            ListItem systemSafetyConstraintItem = new ListItem();
            systemSafetyConstraintItem.add(systemSafetyConstraintParagraph);
            systemSafetyConstraintsList.add(systemSafetyConstraintItem);
        }

        document.add(new Paragraph("\n"));
        document.add(new Paragraph("System Safety Constraints")
                .setBold()
                .setUnderline());
        document.add(systemSafetyConstraintsList);
    }
}
