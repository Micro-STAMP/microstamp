package microstamp.step4.unit;

import lombok.SneakyThrows;
import microstamp.step3.dto.unsafecontrolaction.UnsafeControlActionReadDto;
import microstamp.step4.dto.export.ExportReadDto;
import microstamp.step4.dto.fourtuple.FourTupleFullReadDto;
import microstamp.step4.dto.fourtuple.FourTupleReadDto;
import microstamp.step4.dto.unsafecontrolaction.UnsafeControlActionFullReadDto;
import microstamp.step4.service.FourTupleService;
import microstamp.step4.service.impl.ExportServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExportServiceUnitTest {

    @InjectMocks
    private ExportServiceImpl service;

    @Mock
    private FourTupleService fourTupleService;

    @Test
    @DisplayName("#exportToJson > When nothing is found > Return empty sections")
    void exportToJsonWhenNothingFoundReturnEmpty() {
        UUID analysisId = UUID.randomUUID();
        when(fourTupleService.findByAnalysisId(analysisId)).thenReturn(List.of());
        when(fourTupleService.findByAnalysisIdSortedByUnsafeControlActions(analysisId)).thenReturn(List.of());

        ExportReadDto response = service.exportToJson(analysisId);

        assertAll(
                () -> assertEquals(analysisId, response.getAnalysisId()),
                () -> assertTrue(response.getFourTuples().isEmpty()),
                () -> assertTrue(response.getUnsafeControlActions().isEmpty())
        );
    }

    @Test
    @DisplayName("#exportToJson > When content is found > Return populated export DTO")
    void exportToJsonWhenFoundReturnPopulated() {
        UUID analysisId = UUID.randomUUID();
        FourTupleFullReadDto ft = FourTupleFullReadDto.builder()
                .id(UUID.randomUUID())
                .code("FT-1")
                .scenario("S")
                .associatedCausalFactor("A")
                .recommendation("R")
                .rationale("Ra")
                .unsafeControlActions(List.of(assembleUca.get()))
                .build();

        UnsafeControlActionFullReadDto uca = UnsafeControlActionFullReadDto.builder()
                .id(UUID.randomUUID())
                .uca_code("U-1")
                .name("UCA")
                .fourTuples(List.of(FourTupleReadDto.builder().id(UUID.randomUUID()).scenario("S").associatedCausalFactor("A").recommendation("R").rationale("Ra").code("FT-1").build()))
                .build();

        when(fourTupleService.findByAnalysisId(analysisId)).thenReturn(List.of(ft));
        when(fourTupleService.findByAnalysisIdSortedByUnsafeControlActions(analysisId)).thenReturn(List.of(uca));

        ExportReadDto response = service.exportToJson(analysisId);

        assertAll(
                () -> assertEquals(analysisId, response.getAnalysisId()),
                () -> assertEquals(1, response.getFourTuples().size()),
                () -> assertEquals("FT-1", response.getFourTuples().getFirst().getCode()),
                () -> assertEquals(1, response.getUnsafeControlActions().size())
        );
    }

    @Test
    @SneakyThrows
    @DisplayName("#exportToPdf > When exporting PDF > Returns binary content with PDF signature")
    void exportToPdfReturnsPdfBytes() {
        UUID analysisId = UUID.randomUUID();
        when(fourTupleService.findByAnalysisId(analysisId)).thenReturn(List.of());
        when(fourTupleService.findByAnalysisIdSortedByUnsafeControlActions(analysisId)).thenReturn(List.of());

        byte[] response = service.exportToPdf(analysisId);

        // Check PDF header starts with %PDF-1.
        byte[] expectedPrefix = new byte[] {37, 80, 68, 70, 45, 49};
        assertArrayEquals(expectedPrefix, Arrays.copyOfRange(response, 0, expectedPrefix.length));
    }

    private final Supplier<UnsafeControlActionReadDto> assembleUca = () -> UnsafeControlActionReadDto.builder()
            .id(UUID.randomUUID())
            .uca_code("U-1")
            .name("UCA")
            .build();
}
