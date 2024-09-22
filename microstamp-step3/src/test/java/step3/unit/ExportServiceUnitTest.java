package step3.unit;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import step3.dto.export.ExportReadDto;
import step3.dto.rule.RuleReadListDto;
import step3.dto.unsafe_control_action.UnsafeControlActionReadDto;
import step3.entity.UCAType;
import step3.service.ExportService;
import step3.service.RuleService;
import step3.service.UnsafeControlActionService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExportServiceUnitTest {

    @InjectMocks
    private ExportService service;

    @Mock
    private UnsafeControlActionService ucaService;

    @Mock
    private RuleService ruleService;

    @Test
    @DisplayName("#exportToJson > When no uca list is found > When no rules are found > Return the export read")
    void exportToJsonWhenNoUcaListIsFoundWhenNoRulesAreFoundReturnTheExportRead() {
        UUID mockAnalysisId = UUID.randomUUID();

        when(ucaService.readAllUCAByAnalysisId(mockAnalysisId)).thenReturn(List.of());
        when(ruleService.readRulesByAnalysisId(mockAnalysisId)).thenReturn(List.of());

        ExportReadDto response = service.exportToJson(mockAnalysisId);

        assertAll(
                () -> assertEquals(mockAnalysisId, response.analysisId()),
                () -> assertTrue(response.unsafeControlActions().isEmpty()),
                () -> assertTrue(response.rules().isEmpty())
        );
    }

    @Test
    @DisplayName("#exportToJson > When the uca list is found > When no rules are found > Return the export read")
    void exportToJsonWhenTheUcaListIsFoundWhenNoRulesAreFoundReturnTheExportRead() {
        UUID mockAnalysisId = UUID.randomUUID();
        List<UnsafeControlActionReadDto> mockUnsafeControlActionRead = List.of(assembleUnsafeControlActionRead.apply(mockAnalysisId),
                assembleUnsafeControlActionRead.apply(mockAnalysisId));

        when(ucaService.readAllUCAByAnalysisId(mockAnalysisId)).thenReturn(mockUnsafeControlActionRead);
        when(ruleService.readRulesByAnalysisId(mockAnalysisId)).thenReturn(List.of());

        ExportReadDto response = service.exportToJson(mockAnalysisId);

        assertAll(
                () -> assertEquals(mockAnalysisId, response.analysisId()),
                () -> assertEquals(2, response.unsafeControlActions().size()),
                () -> assertEquals(mockUnsafeControlActionRead, response.unsafeControlActions()),
                () -> assertTrue(response.rules().isEmpty())
        );
    }

    @Test
    @DisplayName("#exportToJson > When no uca list is found > When the rules are found > Return the export read")
    void exportToJsonWhenNoUcaListIsFoundWhenTheRulesAreFoundReturnTheExportRead() {
        UUID mockAnalysisId = UUID.randomUUID();
        List<RuleReadListDto> mockRuleReadList = List.of(assembleRuleReadList.apply(mockAnalysisId), assembleRuleReadList.apply(mockAnalysisId));

        when(ucaService.readAllUCAByAnalysisId(mockAnalysisId)).thenReturn(List.of());
        when(ruleService.readRulesByAnalysisId(mockAnalysisId)).thenReturn(mockRuleReadList);

        ExportReadDto response = service.exportToJson(mockAnalysisId);

        assertAll(
                () -> assertEquals(mockAnalysisId, response.analysisId()),
                () -> assertEquals(2, response.rules().size()),
                () -> assertEquals(mockRuleReadList, response.rules()),
                () -> assertTrue(response.unsafeControlActions().isEmpty())
        );
    }

    @Test
    @DisplayName("#exportToJson > When the uca list is found > When the rules are found > Return the export read")
    void exportToJsonWhenTheUcaListIsFoundWhenTheRulesAreFoundReturnTheExportRead() {
        UUID mockAnalysisId = UUID.randomUUID();
        List<UnsafeControlActionReadDto> mockUnsafeControlActionRead = List.of(assembleUnsafeControlActionRead.apply(mockAnalysisId),
                assembleUnsafeControlActionRead.apply(mockAnalysisId));
        List<RuleReadListDto> mockRuleReadList = List.of(assembleRuleReadList.apply(mockAnalysisId), assembleRuleReadList.apply(mockAnalysisId));

        when(ucaService.readAllUCAByAnalysisId(mockAnalysisId)).thenReturn(mockUnsafeControlActionRead);
        when(ruleService.readRulesByAnalysisId(mockAnalysisId)).thenReturn(mockRuleReadList);

        ExportReadDto response = service.exportToJson(mockAnalysisId);

        assertAll(
                () -> assertEquals(mockAnalysisId, response.analysisId()),
                () -> assertEquals(2, response.unsafeControlActions().size()),
                () -> assertEquals(2, response.rules().size()),
                () -> assertEquals(mockUnsafeControlActionRead, response.unsafeControlActions()),
                () -> assertEquals(mockRuleReadList, response.rules())
        );
    }

    @Test
    @SneakyThrows
    @DisplayName("#exportToPdf > When an pdf analysis is required to export > Export the analysis for step 3")
    void exportToPdfWhenAnPdfAnalysisIsRequiredToExportExportTheAnalysisForStep3() {
        UUID mockAnalysisId = UUID.randomUUID();
        byte[] expected = new byte[] {37, 80, 68, 70, 45, 49, 46, 55, 10, 37, -30, -29, -49, -45, 10, 53, 32, 48, 32, 111, 98, 106, 10, 60, 60, 47, 70, 105, 108, 116, 101, 114, 47, 70, 108, 97, 116, 101, 68, 101, 99, 111, 100, 101, 47, 76, 101, 110, 103, 116, 104, 32, 49, 55, 54, 62, 62, 115, 116, 114, 101, 97, 109, 10, 120, -100, 125, -114, -71, 10, -62, 64, 24, -124, -5, -1, 41, -90, -116, -123, -21, 30, 110, 54, 41, -93, -88, 4, -116, -41, 110, 99, 25, 114, -120, 34, 89, 76, 12, -66};

        byte[] response = service.exportToPdf(mockAnalysisId);

        assertAll(
                () -> verify(ucaService, times(1)).readAllUCAByAnalysisId(mockAnalysisId),
                () -> verify(ruleService, times(1)).readRulesByAnalysisId(mockAnalysisId),
                () -> assertArrayEquals(expected, Arrays.copyOfRange(response, 0, 100))
        );
    }

    private final Function<UUID, UnsafeControlActionReadDto> assembleUnsafeControlActionRead = (analysisId) -> UnsafeControlActionReadDto.builder()
            .id(UUID.randomUUID())
            .analysis_id(analysisId)
            .hazard_code("H-1")
            .constraintName("Mock Constraint")
            .type(UCAType.NOT_PROVIDED.name())
            .rule_code("R1")
            .states(List.of())
            .build();

    private final Function<UUID, RuleReadListDto> assembleRuleReadList = (analysisId) -> RuleReadListDto.builder()
            .id(UUID.randomUUID())
            .code("R1")
            .types(new HashSet<>() {{ add(UCAType.NOT_PROVIDED); }})
            .control_action_name("Mock UCA name")
            .states(List.of())
            .build();


}
