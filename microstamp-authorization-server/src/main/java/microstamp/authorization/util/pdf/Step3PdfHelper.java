package microstamp.authorization.util.pdf;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import microstamp.authorization.dto.step2.StateReadDto;
import microstamp.authorization.dto.step3.RuleReadListDto;
import microstamp.authorization.dto.step3.UnsafeControlActionReadDto;

import java.io.IOException;
import java.util.List;

public class Step3PdfHelper {
    public static void setUcaAndConstraintSection(Document document, List<UnsafeControlActionReadDto> ucaList) throws IOException {
        if (ucaList.isEmpty()) {
            document.add(new Paragraph("No unsafe control actions found"));
            return;
        }

        Table table = new Table(UnitValue.createPercentArray(new float[]{50, 50}))
                .useAllAvailableWidth();

        // Adding header
        table.addHeaderCell("Unsafe Control Action");
        table.addHeaderCell("Safety Constraint");

        String frontendHazardUrl = "127.0.0.1:5173/analyses/" + ucaList.getFirst().analysis_id() + "/purpose";

        // Populating table rows
        for (UnsafeControlActionReadDto uca : ucaList) {
            Paragraph ucaContent = new Paragraph(uca.name());

            if (uca.rule_code() != null && !uca.rule_code().isEmpty()) {
                String ruleCode = " [" + uca.rule_code() + "]";
                ucaContent.add(ruleCode);
            }

            if (uca.hazard_code() != null && !uca.hazard_code().isEmpty()) {
                String hazardCode = " [" + uca.hazard_code() + "]";
                Link hazardLink = new Link(hazardCode, PdfAction.createURI(frontendHazardUrl));
                hazardLink.setFontColor(ColorConstants.BLUE);
                ucaContent.add(hazardLink);
            }

            table.addCell(ucaContent);
            table.addCell(uca.constraintName());

//            StringBuilder ucaName = new StringBuilder(uca.name());
//            ucaName.append(" [").append(uca.hazard_code()).append("]");
//            if (!uca.rule_code().isEmpty()) {
//                ucaName.append("[").append(uca.rule_code()).append("]");
//            }
//
//            table.addCell(ucaName.toString());
//            table.addCell(uca.constraintName());
        }

        document.add(table);
        document.add(new Paragraph("\n"));
    }

    public static void setRuleSection(Document document,  List<RuleReadListDto> ruleList) {
        if (ruleList.isEmpty()) {
            document.add(new Paragraph("No rules found"));
            return;
        }

        Table table = new Table(UnitValue.createPercentArray(new float[]{10, 15, 15, 15, 15, 15}))
                .useAllAvailableWidth();

        table.addHeaderCell("Rule");
        table.addHeaderCell("Control Action Name");
        table.addHeaderCell("States");
        table.addHeaderCell("Types");
        table.addHeaderCell("Hazard");
        table.addHeaderCell("Code");

        List<String> statesNames = ruleList.get(0)
                .states()
                .stream()
                .map(StateReadDto::getName)
                .toList();

        for (RuleReadListDto rule : ruleList) {
            table.addCell(rule.name());
            table.addCell(rule.control_action_name());
            table.addCell(String.join(", ", statesNames));
            table.addCell(rule.types().toString());
            table.addCell(rule.hazard().getName());
            table.addCell(rule.code());
        }

        document.add(table);
        document.add(new Paragraph("\n"));
    }
}
