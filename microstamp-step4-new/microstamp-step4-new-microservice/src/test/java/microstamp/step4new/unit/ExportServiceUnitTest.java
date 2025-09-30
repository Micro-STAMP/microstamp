package microstamp.step4new.unit;

import microstamp.step3.dto.UnsafeControlActionFullReadDto;
import microstamp.step4new.client.MicroStampAuthClient;
import microstamp.step4new.client.MicroStampStep3Client;
import microstamp.step4new.dto.analysis.AnalysisReadDto;
import microstamp.step4new.dto.export.ExportReadDto;
import microstamp.step4new.dto.formalscenario.FormalScenarioReadDto;
import microstamp.step4new.dto.highlevelsolution.HighLevelSolutionReadDto;
import microstamp.step4new.dto.mitigation.MitigationReadDto;
import microstamp.step4new.dto.refinedscenario.RefinedScenarioReadDto;
import microstamp.step4new.service.FormalScenarioService;
import microstamp.step4new.service.HighLevelSolutionService;
import microstamp.step4new.service.MitigationService;
import microstamp.step4new.service.RefinedScenarioService;
import microstamp.step4new.service.impl.ExportServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExportServiceUnitTest {

    @InjectMocks
    private ExportServiceImpl service;

    @Mock
    private MicroStampAuthClient authClient;

    @Mock
    private MicroStampStep3Client step3Client;

    @Mock
    private FormalScenarioService formalScenarioService;

    @Mock
    private HighLevelSolutionService highLevelSolutionService;

    @Mock
    private RefinedScenarioService refinedScenarioService;

    @Mock
    private MitigationService mitigationService;

    @Test
    @DisplayName("#exportToJson > When no UCAs are found > Return export DTO with empty lists")
    void exportToJsonWhenNoUcasAreFoundReturnExportDtoWithEmptyLists() {
        UUID analysisId = UUID.randomUUID();

        when(step3Client.readAllUCAByAnalysisId(analysisId)).thenReturn(List.of());

        ExportReadDto response = service.exportToJson(analysisId);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(analysisId, response.getAnalysisId()),
                () -> assertTrue(response.getHighLevelScenarios().isEmpty()),
                () -> assertTrue(response.getHighLevelSolution().isEmpty()),
                () -> assertTrue(response.getRefinedScenarios().isEmpty()),
                () -> assertTrue(response.getRefinedSolutions().isEmpty())
        );

        verify(step3Client, times(1)).readAllUCAByAnalysisId(analysisId);
        verify(formalScenarioService, never()).getOrCreate(any());
        verify(highLevelSolutionService, never()).getOrCreateByUnsafeControlActionId(any());
        verify(refinedScenarioService, never()).findByUnsafeControlActionId(any());
        verify(mitigationService, never()).findByUnsafeControlActionId(any());
    }

    @Test
    @DisplayName("#exportToJson > When UCAs are found > Return export DTO with populated lists")
    void exportToJsonWhenUcasAreFoundReturnExportDtoWithPopulatedLists() {
        UUID analysisId = UUID.randomUUID();
        UUID ucaId = UUID.randomUUID();
        UnsafeControlActionFullReadDto uca = assembleUca.apply(ucaId);
        FormalScenarioReadDto formalScenario = assembleFormalScenario.get();
        HighLevelSolutionReadDto highLevelSolution = assembleHighLevelSolution.get();
        RefinedScenarioReadDto refinedScenario = assembleRefinedScenario.get();
        MitigationReadDto mitigation = assembleMitigation.get();

        when(step3Client.readAllUCAByAnalysisId(analysisId)).thenReturn(List.of(uca));
        when(formalScenarioService.getOrCreate(ucaId)).thenReturn(formalScenario);
        when(highLevelSolutionService.getOrCreateByUnsafeControlActionId(ucaId)).thenReturn(List.of(highLevelSolution));
        when(refinedScenarioService.findByUnsafeControlActionId(ucaId)).thenReturn(List.of(refinedScenario));
        when(mitigationService.findByUnsafeControlActionId(ucaId)).thenReturn(List.of(mitigation));

        ExportReadDto response = service.exportToJson(analysisId);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(analysisId, response.getAnalysisId()),
                () -> assertEquals(1, response.getHighLevelScenarios().size()),
                () -> assertEquals(1, response.getHighLevelSolution().size()),
                () -> assertEquals(1, response.getRefinedScenarios().size()),
                () -> assertEquals(1, response.getRefinedSolutions().size()),
                () -> assertEquals(formalScenario, response.getHighLevelScenarios().getFirst()),
                () -> assertEquals(highLevelSolution.getId(), response.getHighLevelSolution().getFirst().getId()),
                () -> assertEquals(refinedScenario.getId(), response.getRefinedScenarios().getFirst().getId()),
                () -> assertEquals(mitigation.getId(), response.getRefinedSolutions().getFirst().getId())
        );

        verify(step3Client, times(1)).readAllUCAByAnalysisId(analysisId);
        verify(formalScenarioService, times(1)).getOrCreate(ucaId);
        verify(highLevelSolutionService, times(1)).getOrCreateByUnsafeControlActionId(ucaId);
        verify(refinedScenarioService, times(1)).findByUnsafeControlActionId(ucaId);
        verify(mitigationService, times(1)).findByUnsafeControlActionId(ucaId);
    }

    @Test
    @DisplayName("#exportToJson > When service throws exception > Continue with other services")
    void exportToJsonWhenServiceThrowsExceptionContinueWithOtherServices() {
        UUID analysisId = UUID.randomUUID();
        UUID ucaId = UUID.randomUUID();
        UnsafeControlActionFullReadDto uca = assembleUca.apply(ucaId);
        HighLevelSolutionReadDto highLevelSolution = assembleHighLevelSolution.get();

        when(step3Client.readAllUCAByAnalysisId(analysisId)).thenReturn(List.of(uca));
        when(formalScenarioService.getOrCreate(ucaId)).thenThrow(new RuntimeException("Service error"));
        when(highLevelSolutionService.getOrCreateByUnsafeControlActionId(ucaId)).thenReturn(List.of(highLevelSolution));
        when(refinedScenarioService.findByUnsafeControlActionId(ucaId)).thenThrow(new RuntimeException("Service error"));
        when(mitigationService.findByUnsafeControlActionId(ucaId)).thenThrow(new RuntimeException("Service error"));

        ExportReadDto response = service.exportToJson(analysisId);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(analysisId, response.getAnalysisId()),
                () -> assertTrue(response.getHighLevelScenarios().isEmpty()),
                () -> assertEquals(1, response.getHighLevelSolution().size()),
                () -> assertTrue(response.getRefinedScenarios().isEmpty()),
                () -> assertTrue(response.getRefinedSolutions().isEmpty())
        );

        verify(step3Client, times(1)).readAllUCAByAnalysisId(analysisId);
        verify(formalScenarioService, times(1)).getOrCreate(ucaId);
        verify(highLevelSolutionService, times(1)).getOrCreateByUnsafeControlActionId(ucaId);
        verify(refinedScenarioService, times(1)).findByUnsafeControlActionId(ucaId);
        verify(mitigationService, times(1)).findByUnsafeControlActionId(ucaId);
    }

    @Test
    @DisplayName("#exportToPdf > When no UCAs are found > Return PDF with no UCAs message")
    void exportToPdfWhenNoUcasAreFoundReturnPdfWithNoUcasMessage() throws IOException {
        UUID analysisId = UUID.randomUUID();
        AnalysisReadDto analysis = assembleAnalysis.apply(analysisId);

        when(authClient.getAnalysisById(analysisId)).thenReturn(analysis);
        when(step3Client.readAllUCAByAnalysisId(analysisId)).thenReturn(List.of());

        byte[] response = service.exportToPdf(analysisId);

        assertNotNull(response);
        assertTrue(response.length > 0);

        verify(authClient, times(1)).getAnalysisById(analysisId);
        verify(step3Client, times(1)).readAllUCAByAnalysisId(analysisId);
        verify(formalScenarioService, never()).getOrCreate(any());
    }

    @Test
    @DisplayName("#exportToPdf > When UCAs are found > Return PDF with UCA sections")
    void exportToPdfWhenUcasAreFoundReturnPdfWithUcaSections() throws IOException {
        UUID analysisId = UUID.randomUUID();
        UUID ucaId = UUID.randomUUID();
        AnalysisReadDto analysis = assembleAnalysis.apply(analysisId);
        UnsafeControlActionFullReadDto uca = assembleUca.apply(ucaId);
        FormalScenarioReadDto formalScenario = assembleFormalScenario.get();

        when(authClient.getAnalysisById(analysisId)).thenReturn(analysis);
        when(step3Client.readAllUCAByAnalysisId(analysisId)).thenReturn(List.of(uca));
        when(formalScenarioService.getOrCreate(ucaId)).thenReturn(formalScenario);
        when(highLevelSolutionService.getOrCreateByUnsafeControlActionId(ucaId)).thenReturn(List.of());
        when(refinedScenarioService.findByUnsafeControlActionId(ucaId)).thenReturn(List.of());
        when(mitigationService.findByUnsafeControlActionId(ucaId)).thenReturn(List.of());

        byte[] response = service.exportToPdf(analysisId);

        assertNotNull(response);
        assertTrue(response.length > 0);

        verify(authClient, times(1)).getAnalysisById(analysisId);
        verify(step3Client, times(1)).readAllUCAByAnalysisId(analysisId);
        verify(formalScenarioService, times(1)).getOrCreate(ucaId);
        verify(highLevelSolutionService, times(1)).getOrCreateByUnsafeControlActionId(ucaId);
        verify(refinedScenarioService, times(1)).findByUnsafeControlActionId(ucaId);
        verify(mitigationService, times(1)).findByUnsafeControlActionId(ucaId);
    }

    private final Function<UUID, UnsafeControlActionFullReadDto> assembleUca = (ucaId) -> UnsafeControlActionFullReadDto.builder()
            .id(ucaId)
            .analysis_id(UUID.randomUUID())
            .name("Test UCA")
            .uca_code("UCA-1")
            .build();

    private final Function<UUID, AnalysisReadDto> assembleAnalysis = (analysisId) -> AnalysisReadDto.builder()
            .id(analysisId)
            .name("Test Analysis")
            .description("Test Description")
            .creationDate(Instant.now())
            .userId(UUID.randomUUID())
            .build();

    private final Supplier<FormalScenarioReadDto> assembleFormalScenario = () -> FormalScenarioReadDto.builder()
            .class1(null)
            .class2(null)
            .class3(null)
            .class4(null)
            .build();

    private final Supplier<HighLevelSolutionReadDto> assembleHighLevelSolution = () -> HighLevelSolutionReadDto.builder()
            .id(UUID.randomUUID())
            .processBehavior("Test solution")
            .build();

    private final Supplier<RefinedScenarioReadDto> assembleRefinedScenario = () -> RefinedScenarioReadDto.builder()
            .id(UUID.randomUUID())
            .refinedScenario("Test refined scenario")
            .code("RSC-1")
            .build();

    private final Supplier<MitigationReadDto> assembleMitigation = () -> MitigationReadDto.builder()
            .id(UUID.randomUUID())
            .mitigation("Test mitigation")
            .code("RSOL-1")
            .refinedScenarioId(UUID.randomUUID())
            .build();
}
