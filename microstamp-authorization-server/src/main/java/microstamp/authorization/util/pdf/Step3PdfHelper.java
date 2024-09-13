package microstamp.authorization.util.pdf;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import microstamp.authorization.dto.step3.RuleReadListDto;
import microstamp.authorization.dto.step3.StateReadDto;
import microstamp.authorization.dto.step3.UnsafeControlActionReadDto;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class Step3PdfHelper {
    //TODO: Step 3 PDF export
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

        // Populating table rows
        for (UnsafeControlActionReadDto uca : ucaList) {
            StringBuilder ucaName = new StringBuilder(uca.name());
            ucaName.append(" [").append(uca.hazard_code()).append("]");
            if (!uca.rule_code().isEmpty()) {
                ucaName.append("[").append(uca.rule_code()).append("]");
            }

            table.addCell(ucaName.toString());
            table.addCell(uca.constraintName());
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
                .map(StateReadDto::name)
                .toList();

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
