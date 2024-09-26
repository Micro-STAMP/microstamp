package microstamp.authorization.unit;

import lombok.SneakyThrows;
import microstamp.authorization.client.MicroStampStep1Client;
import microstamp.authorization.client.MicroStampStep2Client;
import microstamp.authorization.client.MicroStampStep3Client;
import microstamp.authorization.dto.AnalysisReadDto;
import microstamp.authorization.dto.ExportReadDto;
import microstamp.authorization.dto.step1.Step1ExportReadDto;
import microstamp.authorization.dto.step2.Step2ExportReadDto;
import microstamp.authorization.dto.step3.Step3ExportReadDto;
import microstamp.authorization.service.AnalysisService;
import microstamp.authorization.service.impl.ExportServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExportServiceImplUnitTest {
    
    @InjectMocks
    private ExportServiceImpl service;

    @Mock
    private AnalysisService analysisService;

    @Mock
    private MicroStampStep1Client step1Client;

    @Mock
    private MicroStampStep2Client step2Client;

    @Mock
    private MicroStampStep3Client step3Client;

    @Test
    @DisplayName("#exportToJson > When it is a guest > Generate the Json")
    void exportToJsonWhenItIsAGuestGenerateTheJson() {
        UUID mockAnalysisId = UUID.randomUUID();
        AnalysisReadDto mockAnalysis = AnalysisReadDto.builder().id(mockAnalysisId).build();
        Step1ExportReadDto mockStep1ExportRead = assembleStep1Export.get();
        Step2ExportReadDto mockStep2ExportRead = assembleStep2Export.get();
        Step3ExportReadDto mockStep3ExportRead = assembleStep3Export.apply(mockAnalysisId);

        when(analysisService.findById(mockAnalysisId)).thenReturn(mockAnalysis);
        when(step1Client.exportStep1ByAnalysisId(mockAnalysisId)).thenReturn(mockStep1ExportRead);
        when(step2Client.exportStep2ByAnalysisId(mockAnalysisId)).thenReturn(mockStep2ExportRead);
        when(step3Client.exportStep3ByAnalysisId(mockAnalysisId)).thenReturn(mockStep3ExportRead);

        ExportReadDto response = service.exportToJson(mockAnalysisId, "");

        assertAll(
                () -> assertEquals(mockAnalysis, response.getAnalysis()),
                () -> assertEquals(mockStep1ExportRead, response.getStep1()),
                () -> assertEquals(mockStep2ExportRead, response.getStep2()),
                () -> assertEquals(mockStep3ExportRead, response.getStep3())
        );
    }

    @Test
    @DisplayName("#exportToJson > When it is logged user > Generate the Json")
    void exportToJsonWhenItIsLoggedUserGenerateTheJson() {
        UUID mockAnalysisId = UUID.randomUUID();
        String mockJwt = "guestJwt";

        AnalysisReadDto mockAnalysis = AnalysisReadDto.builder().id(mockAnalysisId).build();
        Step1ExportReadDto mockStep1ExportRead = assembleStep1Export.get();
        Step2ExportReadDto mockStep2ExportRead = assembleStep2Export.get();
        Step3ExportReadDto mockStep3ExportRead = assembleStep3Export.apply(mockAnalysisId);

        when(analysisService.findById(mockAnalysisId)).thenReturn(mockAnalysis);
        when(step1Client.exportStep1ByAnalysisId(mockJwt, mockAnalysisId)).thenReturn(mockStep1ExportRead);
        when(step2Client.exportStep2ByAnalysisId(mockJwt, mockAnalysisId)).thenReturn(mockStep2ExportRead);
        when(step3Client.exportStep3ByAnalysisId(mockJwt, mockAnalysisId)).thenReturn(mockStep3ExportRead);

        ExportReadDto response = service.exportToJson(mockAnalysisId, mockJwt);

        assertAll(
                () -> assertEquals(mockAnalysis, response.getAnalysis()),
                () -> assertEquals(mockStep1ExportRead, response.getStep1()),
                () -> assertEquals(mockStep2ExportRead, response.getStep2()),
                () -> assertEquals(mockStep3ExportRead, response.getStep3())
        );
    }

    @Test
    @SneakyThrows
    @DisplayName("#exportToPdf > When it is a guest without jwt > Generate the PDF")
    void exportToPdfWhenItIsAGuestWithoutJwtGenerateThePdf() {
        UUID mockAnalysisId = UUID.randomUUID();

        AnalysisReadDto mockAnalysis = AnalysisReadDto.builder().id(mockAnalysisId).build();
        Step1ExportReadDto mockStep1ExportRead = assembleStep1Export.get();
        Step2ExportReadDto mockStep2ExportRead = assembleStep2Export.get();
        Step3ExportReadDto mockStep3ExportRead = assembleStep3Export.apply(mockAnalysisId);

        when(analysisService.findById(mockAnalysisId)).thenReturn(mockAnalysis);
        when(step1Client.exportStep1ByAnalysisId(mockAnalysisId)).thenReturn(mockStep1ExportRead);
        when(step2Client.exportStep2ByAnalysisId(mockAnalysisId)).thenReturn(mockStep2ExportRead);
        when(step3Client.exportStep3ByAnalysisId(mockAnalysisId)).thenReturn(mockStep3ExportRead);

        assertDoesNotThrow(() -> service.exportToPdf(mockAnalysisId, ""));
    }

    @Test
    @DisplayName("#exportToPdf > When it is a logged in user > Generate the PDF")
    void exportToPdfWhenItIsALoggedInUserGenerateThePdf() {
        UUID mockAnalysisId = UUID.randomUUID();
        String mockJwt = "guestJwt";

        AnalysisReadDto mockAnalysis = AnalysisReadDto.builder().id(mockAnalysisId).build();
        Step1ExportReadDto mockStep1ExportRead = assembleStep1Export.get();
        Step2ExportReadDto mockStep2ExportRead = assembleStep2Export.get();
        Step3ExportReadDto mockStep3ExportRead = assembleStep3Export.apply(mockAnalysisId);

        when(analysisService.findById(mockAnalysisId)).thenReturn(mockAnalysis);
        when(step1Client.exportStep1ByAnalysisId(mockJwt, mockAnalysisId)).thenReturn(mockStep1ExportRead);
        when(step2Client.exportStep2ByAnalysisId(mockJwt, mockAnalysisId)).thenReturn(mockStep2ExportRead);
        when(step3Client.exportStep3ByAnalysisId(mockJwt, mockAnalysisId)).thenReturn(mockStep3ExportRead);

        assertDoesNotThrow(() -> service.exportToPdf(mockAnalysisId, mockJwt));
    }


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

}
