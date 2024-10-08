package microstamp.authorization.unit;

import lombok.SneakyThrows;
import microstamp.authorization.dto.AnalysisReadDto;
import microstamp.authorization.dto.ExportReadDto;
import microstamp.authorization.dto.ImageReadDto;
import microstamp.authorization.dto.step1.Step1ExportReadDto;
import microstamp.authorization.dto.step2.Step2ExportReadDto;
import microstamp.authorization.dto.step3.Step3ExportReadDto;
import microstamp.authorization.exception.NotFoundException;
import microstamp.authorization.service.AnalysisService;
import microstamp.authorization.service.ExportService;
import microstamp.authorization.service.impl.GuestServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.test.context.TestPropertySource;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {"security.issuer-uri = http://localhost:9000"})
public class GuestServiceImplUnitTest {

    @InjectMocks
    private GuestServiceImpl service;

    @Mock
    private JwtEncoder jwtEncoder;

    @Mock
    private ExportService exportService;

    @Mock
    private AnalysisService analysisService;

    @Test
    @DisplayName("#findAll > When no analysis exist > Return an empty list")
    void findAllWhenNoAnalysisExistReturnAnEmptyList() {
        when(analysisService.findGuestAnalyses()).thenReturn(List.of());

        List<AnalysisReadDto> response = service.findAll();

        assertTrue(response.isEmpty());
    }

    @Test
    @DisplayName("#findAll > When the guest analysis exist > Return the analysis")
    void findAllWhenTheGuestAnalysisExistReturnTheAnalysis() {
        AnalysisReadDto mock = assembleAnalysisRead.get();

        when(analysisService.findGuestAnalyses()).thenReturn(List.of(mock));

        List<AnalysisReadDto> response = service.findAll();
        assertAll(
                () -> assertEquals(1, response.size()),
                () -> assertEquals(mock, response.getFirst())
        );
    }

    @Test
    @DisplayName("#findById > When no guest is found > Throw an exception")
    void findByIdWhenNoGuestIsFoundThrowAnException() {
        UUID mockAnalysisId = UUID.randomUUID();

        when(analysisService.findGuestAnalyses()).thenReturn(List.of(assembleAnalysisRead.get()));

        assertThrows(NotFoundException.class, () -> service.findById(mockAnalysisId));
    }

    @Test
    @DisplayName("#findById > When the guest analysis is found > Return it")
    void findByIdWhenTheGuestAnalysisIsFoundReturnIt() {
        AnalysisReadDto mock = assembleAnalysisRead.get();

        when(analysisService.findGuestAnalyses()).thenReturn(List.of(mock));
        when(analysisService.findById(mock.getId())).thenReturn(mock);

        AnalysisReadDto response = service.findById(mock.getId());
        assertEquals(mock, response);
    }

    @Test
    @DisplayName("#exportAll > When no analysis is found > Return an empty list")
    void exportAllWhenNoAnalysisIsFoundReturnAnEmptyList() {
        when(analysisService.findGuestAnalyses()).thenReturn(List.of());

        List<ExportReadDto> response = service.exportAll();

        assertTrue(response.isEmpty());
    }

    @Test
    @DisplayName("#exportAll > When an analysis is found > Return a list with the analysis exported")
    void exportAllWhenAnAnalysisIsFoundReturnAListWithTheAnalysisExported() {
        ExportReadDto mock = assembleExportRead.get();
        String mockToken = "mockedGuestToken";
        service.setIssuerUri("http://localhost:9000");

        when(analysisService.findGuestAnalyses()).thenReturn(List.of(mock.getAnalysis()));
        when(exportService.exportToJson(mock.getAnalysis().getId(), "Bearer ".concat(mockToken))).thenReturn(mock);
        when(jwtEncoder.encode(any())).thenReturn(new Jwt(mockToken, null, null, new HashMap<>() {{ put("Teste", "Teste"); }}, new HashMap<>() {{ put("Teste", "Teste"); }}));

        List<ExportReadDto> response = service.exportAll();

        assertAll(
                () -> assertEquals(1, response.size()),
                () -> assertEquals(mock, response.getFirst())
        );
    }

    @Test
    @DisplayName("#exportToJson > When no guest is found > Throw an exception")
    void exportToJsonWhenNoGuestIsFoundThrowAnException() {
        UUID mockAnalysisId = UUID.randomUUID();

        when(analysisService.findGuestAnalyses()).thenReturn(List.of(assembleAnalysisRead.get()));

        assertThrows(NotFoundException.class, () -> service.exportToJson(mockAnalysisId));
    }

    @Test
    @DisplayName("#exportToJson > When the guest analysis is found > Return it")
    void exportToJsonWhenTheGuestAnalysisIsFoundReturnIt() {
        String mockToken = "mockedGuestToken";
        AnalysisReadDto mock = assembleAnalysisRead.get();
        ExportReadDto expected = assembleExportRead.get();
        service.setIssuerUri("http://localhost:9000");

        when(analysisService.findGuestAnalyses()).thenReturn(List.of(mock));
        when(exportService.exportToJson(mock.getId(), "Bearer ".concat(mockToken))).thenReturn(expected);
        when(jwtEncoder.encode(any())).thenReturn(new Jwt(mockToken, null, null, new HashMap<>() {{ put("Teste", "Teste"); }}, new HashMap<>() {{ put("Teste", "Teste"); }}));

        ExportReadDto response = service.exportToJson(mock.getId());

        assertEquals(expected, response);
    }

    @Test
    @SneakyThrows
    @DisplayName("#exportToPdf > When the guest analysis is found > Return it")
    void exportToPdfWhenTheGuestAnalysisIsFoundReturnIt() {
        String mockToken = "mockedGuestToken";
        AnalysisReadDto mock = assembleAnalysisRead.get();
        ExportReadDto expected = assembleExportRead.get();
        service.setIssuerUri("http://localhost:9000");

        when(analysisService.findGuestAnalyses()).thenReturn(List.of(mock));
        when(jwtEncoder.encode(any())).thenReturn(new Jwt(mockToken, null, null, new HashMap<>() {{ put("Teste", "Teste"); }}, new HashMap<>() {{ put("Teste", "Teste"); }}));

        assertDoesNotThrow(() -> service.exportToPdf(mock.getId()));
    }

    @Test
    @DisplayName("#exportToPdf > When no guest is found > Throw an exception")
    void exportToPdfWhenNoGuestIsFoundThrowAnException() {
        UUID mockAnalysisId = UUID.randomUUID();

        when(analysisService.findGuestAnalyses()).thenReturn(List.of(assembleAnalysisRead.get()));

        assertThrows(NotFoundException.class, () -> service.exportToPdf(mockAnalysisId));
    }

    private final Supplier<AnalysisReadDto> assembleAnalysisRead = () -> AnalysisReadDto.builder()
            .id(UUID.randomUUID())
            .name("Mock analysis")
            .creationDate(Instant.now())
            .userId(UUID.randomUUID())
            .image(ImageReadDto.builder()
                    .id(UUID.randomUUID())
                    .fileName("image.png")
                    .base64("mockBase64")
                    .build())
            .build();

    private final Supplier<Step1ExportReadDto> assembleStep1Export = () -> Step1ExportReadDto.builder()
            .assumptions(List.of())
            .hazards(List.of())
            .losses(List.of())
            .systemGoals(List.of())
            .systemSafetyConstraints(List.of())
            .build();

    private final Supplier<Step2ExportReadDto> assembleStep2Export = () -> Step2ExportReadDto.builder()
            .components(List.of())
            .connections(List.of())
            .images(List.of())
            .build();

    private final Function<UUID, Step3ExportReadDto> assembleStep3Export = (mockAnalysisId) -> Step3ExportReadDto.builder()
            .rules(List.of())
            .unsafeControlActions(List.of())
            .analysisId(mockAnalysisId)
            .build();

    private final Supplier<ExportReadDto> assembleExportRead = () -> {
        AnalysisReadDto analysis = assembleAnalysisRead.get();
        return ExportReadDto.builder()
                .analysis(analysis)
                .step1(assembleStep1Export.get())
                .step2(assembleStep2Export.get())
                .step3(assembleStep3Export.apply(analysis.getId()))
                .build();
    };

}
