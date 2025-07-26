package microstamp.authorization.util.pdf;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.WebColors;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
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

        table.addHeaderCell("Unsafe Control Action");
        table.addHeaderCell("Safety Constraint");

        for (UnsafeControlActionReadDto uca : ucaList) {
            String ucaCode = "[" + uca.uca_code() + "] ";
            Paragraph ucaContent = new Paragraph(ucaCode + uca.name());
            ucaContent.setDestination(uca.uca_code());

            if (uca.rule_code() != null && !uca.rule_code().isEmpty()) {
                String ruleCode = " [" + uca.rule_code() + "]";
                Link ruleLink = new Link(ruleCode, PdfAction.createGoTo(uca.rule_code()));
                ruleLink.setFontColor(WebColors.getRGBColor("#b4894d"));
                ucaContent.add(ruleLink);
            }

            if (uca.hazard_code() != null && !uca.hazard_code().isEmpty()) {
                String hazardCode = " [" + uca.hazard_code() + "]";
                Link hazardLink = new Link(hazardCode, PdfAction.createGoTo(uca.hazard_code()));
                hazardLink.setFontColor(WebColors.getRGBColor("#b4894d"));
                ucaContent.add(hazardLink);
            }

            table.addCell(ucaContent);

            String constraintCode = "[" + uca.constraint_code() + "] ";
            Paragraph constraintContent = new Paragraph(constraintCode + uca.constraintName());
            constraintContent.setDestination(uca.constraint_code());
            table.addCell(constraintContent);
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

            Paragraph ruleCode = new Paragraph(rule.code()).setDestination(rule.code());
            table.addCell(ruleCode);
        }

        document.add(table);
        document.add(new Paragraph("\n"));
    }
}
