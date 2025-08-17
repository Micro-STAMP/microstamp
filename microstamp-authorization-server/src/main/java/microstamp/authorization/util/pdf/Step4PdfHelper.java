package microstamp.authorization.util.pdf;

import com.itextpdf.kernel.colors.WebColors;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import microstamp.authorization.dto.step4.FourTupleFullReadDto;
import microstamp.authorization.dto.step4.FourTupleReadDto;
import microstamp.authorization.dto.step4.UnsafeControlActionFullReadDto;

import java.util.List;

public class Step4PdfHelper {

    private static final float[] FOUR_TUPLE_COLUMN_WIDTHS = new float[]{8f, 71f, 21f};

    private static final float[] UCA_COLUMN_WIDTHS = new float[]{8f, 23f, 23f, 23f, 23f};

    public static void setFourTuplesSection(Document document, List<FourTupleFullReadDto> fourTuples) {
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

            Paragraph ucaCodeContent = new Paragraph();

            if (fourTuple.getUnsafeControlActions() != null && !fourTuple.getUnsafeControlActions().isEmpty()) {
                fourTuple.getUnsafeControlActions().stream()
                        .map(uca -> {
                            Link ucaLink = new Link("[" + uca.uca_code() + "]", PdfAction.createGoTo(uca.uca_code()));
                            ucaLink.setFontColor(WebColors.getRGBColor("#b4894d"));
                            return ucaLink;
                        })
                        .forEach(ucaLink -> ucaCodeContent.add(ucaLink).add(" "));
            }

            table.addCell(fourTuple.getCode());
            table.addCell(description);
            table.addCell(ucaCodeContent);
        }

        document.add(table);
    }

    public static void setUnsafeControlActionsSection(Document document, List<UnsafeControlActionFullReadDto> unsafeControlActions) {
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

            if (unsafeControlAction.getFourTuples().isEmpty()) {
                document.add(new Paragraph("\nNo scenario identified for this UCA"));
            } else {
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
}
