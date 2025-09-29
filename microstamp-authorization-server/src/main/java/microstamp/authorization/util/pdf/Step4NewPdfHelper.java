package microstamp.authorization.util.pdf;

import com.itextpdf.kernel.colors.WebColors;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import microstamp.authorization.client.MicroStampStep3Client;
import microstamp.authorization.dto.step3.UnsafeControlActionFullReadDto;
import microstamp.authorization.dto.step4new.FormalScenarioReadDto;
import microstamp.authorization.dto.step4new.FormalScenarioClassReadDto;
import microstamp.authorization.dto.step4new.HighLevelSolutionReadDto;
import microstamp.authorization.dto.step4new.MitigationReadDto;
import microstamp.authorization.dto.step4new.RefinedScenarioReadDto;
import microstamp.authorization.dto.step4new.Step4NewExportReadDto;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class Step4NewPdfHelper {

    public static void setStep4NewContent(Document document, Step4NewExportReadDto step4NewDto, MicroStampStep3Client step3Client) {
        if (step4NewDto.getAnalysisId() == null) {
            document.add(new Paragraph("No analysis data found."));
            return;
        }

        List<UnsafeControlActionFullReadDto> ucas = step3Client.readAllUCAByAnalysisId(step4NewDto.getAnalysisId());

        if (ucas.isEmpty()) {
            document.add(new Paragraph("No unsafe control actions found for this analysis"));
            return;
        }

        for (UnsafeControlActionFullReadDto uca : ucas)
            setUcaSection(document, uca, step4NewDto);
    }

    private static void setUcaSection(Document document, UnsafeControlActionFullReadDto uca, Step4NewExportReadDto step4NewDto) {
        UUID ucaId = uca.getId();

        document.add(new Paragraph("\n"));
        document.add(new Paragraph("[" + uca.getUca_code() + "] " + uca.getName())
                .setBold()
                .setFontSize(12));

        FormalScenarioReadDto formalScenario = findFormalScenarioByUca(step4NewDto.getHighLevelScenarios());
        List<HighLevelSolutionReadDto> ucaHighLevelSolutions = findHighLevelSolutionsByUca(step4NewDto.getHighLevelSolution());
        List<RefinedScenarioReadDto> ucaRefinedScenarios = findRefinedScenariosByUca(step4NewDto.getRefinedScenarios(), ucaId);
        List<MitigationReadDto> ucaMitigations = findMitigationsByUca(step4NewDto.getRefinedSolutions(), ucaRefinedScenarios);

        setClassSection(document, "Class 1 - Unsafe Controller Behavior",
            formalScenario != null ? formalScenario.getClass1() : null,
            getHighLevelSolutionByClass(ucaHighLevelSolutions),
            filterRefinedScenariosByClass(ucaRefinedScenarios, formalScenario, "class1"),
            filterMitigationsByClass(ucaMitigations, ucaRefinedScenarios, formalScenario, "class1"));

        setClassSection(document, "Class 2 - Unsafe Feedback Path",
            formalScenario != null ? formalScenario.getClass2() : null,
            getHighLevelSolutionByClass(ucaHighLevelSolutions),
            filterRefinedScenariosByClass(ucaRefinedScenarios, formalScenario, "class2"),
            filterMitigationsByClass(ucaMitigations, ucaRefinedScenarios, formalScenario, "class2"));

        setClassSection(document, "Class 3 - Unsafe Control Path",
            formalScenario != null ? formalScenario.getClass3() : null,
            getHighLevelSolutionByClass(ucaHighLevelSolutions),
            filterRefinedScenariosByClass(ucaRefinedScenarios, formalScenario, "class3"),
            filterMitigationsByClass(ucaMitigations, ucaRefinedScenarios, formalScenario, "class3"));

        setClassSection(document, "Class 4 - Unsafe Controlled Process Behavior",
            formalScenario != null ? formalScenario.getClass4() : null,
            getHighLevelSolutionByClass(ucaHighLevelSolutions),
            filterRefinedScenariosByClass(ucaRefinedScenarios, formalScenario, "class4"),
            filterMitigationsByClass(ucaMitigations, ucaRefinedScenarios, formalScenario, "class4"));
    }

    private static void setClassSection(Document document, String classTitle,
                                      FormalScenarioClassReadDto classScenario,
                                      HighLevelSolutionReadDto classHighLevelSolution,
                                      List<RefinedScenarioReadDto> classRefinedScenarios,
                                      List<MitigationReadDto> classMitigations) {

        document.add(new Paragraph("\n"));
        document.add(new Paragraph(classTitle)
                .setBold()
                .setFontSize(13));

        set41HighLevelScenariosActivity(document, classScenario);
        set42HighLevelSolutionsActivity(document, classHighLevelSolution);
        set43RefinedScenariosActivity(document, classRefinedScenarios);
        set44RefinedSolutionsActivity(document, classMitigations);
    }

    private static FormalScenarioReadDto findFormalScenarioByUca(List<FormalScenarioReadDto> scenarios) {
        return scenarios.isEmpty() ? null : scenarios.getFirst();
    }

    private static List<HighLevelSolutionReadDto> findHighLevelSolutionsByUca(List<HighLevelSolutionReadDto> solutions) {
        return solutions;
    }

    private static List<RefinedScenarioReadDto> findRefinedScenariosByUca(List<RefinedScenarioReadDto> scenarios, UUID ucaId) {
        return scenarios.stream()
            .filter(scenario -> ucaId.equals(scenario.getUnsafeControlActionId()))
            .collect(Collectors.toList());
    }

    private static List<MitigationReadDto> findMitigationsByUca(List<MitigationReadDto> mitigations, List<RefinedScenarioReadDto> ucaScenarios) {
        Set<UUID> ucaScenarioIds = ucaScenarios.stream()
            .map(RefinedScenarioReadDto::getId)
            .collect(Collectors.toSet());

        return mitigations.stream()
            .filter(mitigation -> ucaScenarioIds.contains(mitigation.getRefinedScenarioId()))
            .collect(Collectors.toList());
    }

    private static HighLevelSolutionReadDto getHighLevelSolutionByClass(List<HighLevelSolutionReadDto> solutions) {
        return solutions.isEmpty() ? null : solutions.getFirst();
    }

    private static List<RefinedScenarioReadDto> filterRefinedScenariosByClass(List<RefinedScenarioReadDto> scenarios,
                                                                            FormalScenarioReadDto formalScenario,
                                                                            String classKey) {
        if (scenarios == null || formalScenario == null)
            return List.of();

        UUID classId = getClassIdByKey(formalScenario, classKey);
        if (classId == null)
            return List.of();

        return scenarios.stream()
            .filter(scenario -> classId.equals(scenario.getFormalScenarioClassId()))
            .collect(Collectors.toList());
    }

    private static List<MitigationReadDto> filterMitigationsByClass(List<MitigationReadDto> mitigations,
                                                                  List<RefinedScenarioReadDto> allScenarios,
                                                                  FormalScenarioReadDto formalScenario,
                                                                  String classKey) {
        List<RefinedScenarioReadDto> classScenarios = filterRefinedScenariosByClass(allScenarios, formalScenario, classKey);
        if (mitigations == null || classScenarios.isEmpty())
            return List.of();

        Set<UUID> classScenarioIds = classScenarios.stream()
            .map(RefinedScenarioReadDto::getId)
            .collect(Collectors.toSet());

        return mitigations.stream()
            .filter(mitigation -> classScenarioIds.contains(mitigation.getRefinedScenarioId()))
            .collect(Collectors.toList());
    }

    private static UUID getClassIdByKey(FormalScenarioReadDto formalScenario, String classKey) {
        return switch (classKey) {
            case "class1" -> formalScenario.getClass1() != null ? formalScenario.getClass1().getId() : null;
            case "class2" -> formalScenario.getClass2() != null ? formalScenario.getClass2().getId() : null;
            case "class3" -> formalScenario.getClass3() != null ? formalScenario.getClass3().getId() : null;
            case "class4" -> formalScenario.getClass4() != null ? formalScenario.getClass4().getId() : null;
            default -> null;
        };
    }

    private static void set41HighLevelScenariosActivity(Document document, FormalScenarioClassReadDto classScenario) {
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("4.1 Identify High-Level Scenarios:")
                .setBold()
                .setFontSize(11));

        if (classScenario == null) {
            document.add(new Paragraph("No high level scenarios found."));
            return;
        }

        document.add(new Paragraph("[Output] " +
            (classScenario.getOutput() != null ? classScenario.getOutput() : "")));
        document.add(new Paragraph("[Input] " +
            (classScenario.getInput() != null ? classScenario.getInput() : "")));
    }

    private static void set42HighLevelSolutionsActivity(Document document, HighLevelSolutionReadDto solution) {
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("4.2 Identify High-Level Solutions:")
                .setBold()
                .setFontSize(11));

        if (solution == null || isHighLevelSolutionEmpty(solution)) {
            document.add(new Paragraph("No high level solutions found."));
            return;
        }

        document.add(new Paragraph("\nController Behavior:")
                .setBold()
                .setFontSize(10));
        document.add(new Paragraph(solution.getControllerBehavior() != null && !solution.getControllerBehavior().trim().isEmpty()
            ? solution.getControllerBehavior() : "-"));

        document.add(new Paragraph("\nProcess Behavior:")
                .setBold()
                .setFontSize(10));
        document.add(new Paragraph(solution.getProcessBehavior() != null && !solution.getProcessBehavior().trim().isEmpty()
            ? solution.getProcessBehavior() : "-"));

        document.add(new Paragraph("\nOther Solutions:")
                .setBold()
                .setFontSize(10));
        document.add(new Paragraph(solution.getOtherSolutions() != null && !solution.getOtherSolutions().trim().isEmpty()
            ? solution.getOtherSolutions() : "-"));
    }

    private static void set43RefinedScenariosActivity(Document document, List<RefinedScenarioReadDto> refinedScenarios) {
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("4.3 Identify Refined Scenarios:")
                .setBold()
                .setFontSize(11));

        if (refinedScenarios == null || refinedScenarios.isEmpty()) {
            document.add(new Paragraph("No refined scenarios found."));
            return;
        }

        document.add(new Paragraph("\nRefined Scenarios:")
                .setBold()
                .setFontSize(10));

        for (RefinedScenarioReadDto scenario : refinedScenarios) {
            String code = scenario.getCode() != null ? scenario.getCode() : "";
            String scenarioText = scenario.getRefinedScenario() != null ? scenario.getRefinedScenario() : "";

            Paragraph scenarioParagraph = new Paragraph("[" + code + "] " + scenarioText);
            scenarioParagraph.setDestination(code);
            document.add(scenarioParagraph);
        }
    }

    private static void set44RefinedSolutionsActivity(Document document, List<MitigationReadDto> refinedSolutions) {
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("4.4 Identify Refined Solutions:")
                .setBold()
                .setFontSize(11));

        if (refinedSolutions == null || refinedSolutions.isEmpty()) {
            document.add(new Paragraph("No refined solutions found."));
            return;
        }

        document.add(new Paragraph("\nRefined Solutions:")
                .setBold()
                .setFontSize(10));

        for (MitigationReadDto solution : refinedSolutions) {
            String code = solution.getCode() != null ? solution.getCode() : "";
            String mitigationText = solution.getMitigation() != null ? solution.getMitigation() : "";
            String dependencyCode = solution.getRefinedScenarioCode() != null ? solution.getRefinedScenarioCode() : "";

            Paragraph solutionParagraph = new Paragraph("[" + code + "] " + mitigationText);

            if (!dependencyCode.isEmpty()) {
                solutionParagraph.add("    ");
                Link scenarioLink = new Link("[" + dependencyCode + "]", PdfAction.createGoTo(dependencyCode));
                scenarioLink.setFontColor(WebColors.getRGBColor("#b4894d"));
                solutionParagraph.add(scenarioLink);
            }

            document.add(solutionParagraph);
        }
    }

    private static boolean isHighLevelSolutionEmpty(HighLevelSolutionReadDto solution) {
        return (solution.getProcessBehavior() == null || solution.getProcessBehavior().trim().isEmpty()) &&
               (solution.getControllerBehavior() == null || solution.getControllerBehavior().trim().isEmpty()) &&
               (solution.getOtherSolutions() == null || solution.getOtherSolutions().trim().isEmpty());
    }
}
