package microstamp.step4new.service.impl;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.WebColors;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.step3.dto.UnsafeControlActionFullReadDto;
import microstamp.step4new.client.MicroStampStep3Client;
import microstamp.step4new.dto.export.ExportReadDto;
import microstamp.step4new.dto.formalscenario.FormalScenarioReadDto;
import microstamp.step4new.dto.formalscenarioclass.FormalScenarioClassReadDto;
import microstamp.step4new.dto.highlevelsolution.HighLevelSolutionReadDto;
import microstamp.step4new.dto.mitigation.MitigationReadDto;
import microstamp.step4new.dto.refinedscenario.RefinedScenarioReadDto;
import microstamp.step4new.service.ExportService;
import microstamp.step4new.service.FormalScenarioService;
import microstamp.step4new.service.HighLevelSolutionService;
import microstamp.step4new.service.MitigationService;
import microstamp.step4new.service.RefinedScenarioService;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@Component
@AllArgsConstructor
public class ExportServiceImpl implements ExportService {

    private final MicroStampStep3Client step3Client;
    private final FormalScenarioService formalScenarioService;
    private final HighLevelSolutionService highLevelSolutionService;
    private final RefinedScenarioService refinedScenarioService;
    private final MitigationService mitigationService;

    @Override
    public ExportReadDto exportToJson(UUID analysisId) {
        log.info("Exporting (JSON) Step 4 New content of an analysis by its UUID: {}", analysisId);
        return getExportDto(analysisId);
    }

    @Override
    public byte[] exportToPdf(UUID analysisId) throws IOException {
        log.info("Exporting (PDF) Step 4 New content of an analysis by its UUID: {}", analysisId);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter pdfWriter = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);

        setTitle(document);
        setAnalysisSection(document, analysisId);

        List<UnsafeControlActionFullReadDto> ucas = step3Client.readAllUCAByAnalysisId(analysisId);
        
        if (ucas.isEmpty()) {
            document.add(new Paragraph("No unsafe control actions found for this analysis"));
        } else {
            for (UnsafeControlActionFullReadDto uca : ucas) {
                setUcaSection(document, uca);
            }
        }

        document.close();

        return byteArrayOutputStream.toByteArray();
    }

    private ExportReadDto getExportDto(UUID analysisId) {
        List<UnsafeControlActionFullReadDto> ucas = step3Client.readAllUCAByAnalysisId(analysisId);
        
        List<FormalScenarioReadDto> highLevelScenarios = new ArrayList<>();
        List<HighLevelSolutionReadDto> highLevelSolution = new ArrayList<>();
        List<RefinedScenarioReadDto> refinedScenarios = new ArrayList<>();
        List<MitigationReadDto> refinedSolutions = new ArrayList<>();

        for (UnsafeControlActionFullReadDto uca : ucas) {
            UUID ucaId = uca.id();

            try {
                FormalScenarioReadDto formalScenario = formalScenarioService.getOrCreate(ucaId);
                highLevelScenarios.add(formalScenario);
            } catch (Exception e) {
                log.warn("No formal scenario found for UCA {}: {}", ucaId, e.getMessage());
            }

            try {
                List<HighLevelSolutionReadDto> ucaHighLevelSolutions = 
                    highLevelSolutionService.getOrCreateByUnsafeControlActionId(ucaId);
                highLevelSolution.addAll(ucaHighLevelSolutions);
            } catch (Exception e) {
                log.warn("No high level solutions found for UCA {}: {}", ucaId, e.getMessage());
            }

            try {
                List<RefinedScenarioReadDto> ucaRefinedScenarios = 
                    refinedScenarioService.findByUnsafeControlActionId(ucaId);
                refinedScenarios.addAll(ucaRefinedScenarios);
            } catch (Exception e) {
                log.warn("No refined scenarios found for UCA {}: {}", ucaId, e.getMessage());
            }

            try {
                List<MitigationReadDto> ucaMitigations = mitigationService.findByUnsafeControlActionId(ucaId);
                refinedSolutions.addAll(ucaMitigations);
            } catch (Exception e) {
                log.warn("No mitigations found for UCA {}: {}", ucaId, e.getMessage());
            }
        }

        return ExportReadDto.builder()
                .analysisId(analysisId)
                .highLevelScenarios(highLevelScenarios)
                .highLevelSolution(highLevelSolution)
                .refinedScenarios(refinedScenarios)
                .refinedSolutions(refinedSolutions)
                .build();
    }

    private void setTitle(Document document) throws IOException {
        Style style = new Style();
        style.setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ITALIC));

        document.add(new Paragraph("MICROSTAMP ANALYSIS - STEP 4 NEW")
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

    private void setUcaSection(Document document, UnsafeControlActionFullReadDto uca) {
        UUID ucaId = uca.id();

        document.add(new Paragraph("\n"));
        document.add(new Paragraph("[" + uca.uca_code() + "] " + uca.name())
                .setBold()
                .setFontSize(12));

        FormalScenarioReadDto formalScenario = null;
        List<HighLevelSolutionReadDto> allHighLevelSolutions = null;
        List<RefinedScenarioReadDto> allRefinedScenarios = null;
        List<MitigationReadDto> allMitigations = null;
        
        try {
            formalScenario = formalScenarioService.getOrCreate(ucaId);
        } catch (Exception e) {
            log.warn("No formal scenario found for UCA {}: {}", ucaId, e.getMessage());
        }
        
        try {
            allHighLevelSolutions = highLevelSolutionService.getOrCreateByUnsafeControlActionId(ucaId);
        } catch (Exception e) {
            log.warn("No high level solutions found for UCA {}: {}", ucaId, e.getMessage());
        }
        
        try {
            allRefinedScenarios = refinedScenarioService.findByUnsafeControlActionId(ucaId);
        } catch (Exception e) {
            log.warn("No refined scenarios found for UCA {}: {}", ucaId, e.getMessage());
        }
        
        try {
            allMitigations = mitigationService.findByUnsafeControlActionId(ucaId);
        } catch (Exception e) {
            log.warn("No mitigations found for UCA {}: {}", ucaId, e.getMessage());
        }

        setClassSection(document, "Class 1 - Unsafe Controller Behavior", 
            formalScenario != null ? formalScenario.getClass1() : null,
            getHighLevelSolutionByClass(allHighLevelSolutions),
            filterRefinedScenariosByClass(allRefinedScenarios, formalScenario, "class1"),
            filterMitigationsByClass(allMitigations, allRefinedScenarios, formalScenario, "class1"));
            
        setClassSection(document, "Class 2 - Unsafe Feedback Path", 
            formalScenario != null ? formalScenario.getClass2() : null,
            getHighLevelSolutionByClass(allHighLevelSolutions),
            filterRefinedScenariosByClass(allRefinedScenarios, formalScenario, "class2"),
            filterMitigationsByClass(allMitigations, allRefinedScenarios, formalScenario, "class2"));
            
        setClassSection(document, "Class 3 - Unsafe Control Path", 
            formalScenario != null ? formalScenario.getClass3() : null,
            getHighLevelSolutionByClass(allHighLevelSolutions),
            filterRefinedScenariosByClass(allRefinedScenarios, formalScenario, "class3"),
            filterMitigationsByClass(allMitigations, allRefinedScenarios, formalScenario, "class3"));
            
        setClassSection(document, "Class 4 - Unsafe Controlled Process Behavior", 
            formalScenario != null ? formalScenario.getClass4() : null,
            getHighLevelSolutionByClass(allHighLevelSolutions),
            filterRefinedScenariosByClass(allRefinedScenarios, formalScenario, "class4"),
            filterMitigationsByClass(allMitigations, allRefinedScenarios, formalScenario, "class4"));
    }
    
    private void setClassSection(Document document, String classTitle, 
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
    
    private HighLevelSolutionReadDto getHighLevelSolutionByClass(List<HighLevelSolutionReadDto> solutions) {
        if (solutions != null && !solutions.isEmpty())
            return solutions.getFirst();
        return null;
    }
    
    private List<RefinedScenarioReadDto> filterRefinedScenariosByClass(List<RefinedScenarioReadDto> scenarios, 
                                                                     FormalScenarioReadDto formalScenario, 
                                                                     String classKey) {
        if (scenarios == null || formalScenario == null)
            return List.of();
        
        UUID classId = getClassIdByKey(formalScenario, classKey);
        if (classId == null)
            return List.of();
        
        return scenarios.stream()
            .filter(scenario -> classId.equals(scenario.getFormalScenarioClassId()))
            .toList();
    }
    
    private List<MitigationReadDto> filterMitigationsByClass(List<MitigationReadDto> mitigations,
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
            .toList();
    }
    
    private UUID getClassIdByKey(FormalScenarioReadDto formalScenario, String classKey) {
        return switch (classKey) {
            case "class1" -> formalScenario.getClass1() != null ? formalScenario.getClass1().getId() : null;
            case "class2" -> formalScenario.getClass2() != null ? formalScenario.getClass2().getId() : null;
            case "class3" -> formalScenario.getClass3() != null ? formalScenario.getClass3().getId() : null;
            case "class4" -> formalScenario.getClass4() != null ? formalScenario.getClass4().getId() : null;
            default -> null;
        };
    }
    
    private boolean isHighLevelSolutionEmpty(HighLevelSolutionReadDto solution) {
        return (solution.getProcessBehavior() == null || solution.getProcessBehavior().trim().isEmpty()) &&
               (solution.getControllerBehavior() == null || solution.getControllerBehavior().trim().isEmpty()) &&
               (solution.getOtherSolutions() == null || solution.getOtherSolutions().trim().isEmpty());
    }

    private void set41HighLevelScenariosActivity(Document document, FormalScenarioClassReadDto classScenario) {
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

    private void set42HighLevelSolutionsActivity(Document document, HighLevelSolutionReadDto solution) {
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

    private void set43RefinedScenariosActivity(Document document, List<RefinedScenarioReadDto> refinedScenarios) {
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

    private void set44RefinedSolutionsActivity(Document document, List<MitigationReadDto> refinedSolutions) {
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
}
