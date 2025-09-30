package microstamp.step4new.unit;

import microstamp.step3.dto.UCAType;
import microstamp.step3.dto.UnsafeControlActionFullReadDto;
import microstamp.step4new.client.MicroStampStep3Client;
import microstamp.step4new.dto.refinedscenario.RefinedScenarioCommonCauseReadDto;
import microstamp.step4new.dto.refinedscenario.RefinedScenarioTemplateReadDto;
import microstamp.step4new.entity.RefinedScenarioCommonCause;
import microstamp.step4new.entity.RefinedScenarioTemplate;
import microstamp.step4new.repository.RefinedScenarioTemplateRepository;
import microstamp.step4new.service.impl.RefinedScenarioTemplateServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RefinedScenarioTemplateServiceUnitTest {

    @InjectMocks
    private RefinedScenarioTemplateServiceImpl service;

    @Mock
    private RefinedScenarioTemplateRepository templateRepository;

    @Mock
    private MicroStampStep3Client step3Client;

    @Test
    @DisplayName("#findByCommonCauseCode > When no templates are found > Return empty list")
    void findByCommonCauseCodeWhenNoTemplatesAreFoundReturnEmptyList() {
        String code = "CC-1";

        when(templateRepository.findByCommonCause_Code(code)).thenReturn(List.of());

        List<RefinedScenarioTemplateReadDto> response = service.findByCommonCauseCode(code);

        assertTrue(response.isEmpty());
        verify(templateRepository, times(1)).findByCommonCause_Code(code);
    }

    @Test
    @DisplayName("#findByCommonCauseCode > When templates are found > Return mapped DTOs")
    void findByCommonCauseCodeWhenTemplatesAreFoundReturnMappedDtos() {
        String code = "CC-1";
        RefinedScenarioTemplate template1 = assembleTemplate.apply(code, null);
        RefinedScenarioTemplate template2 = assembleTemplate.apply(code, UCAType.PROVIDED);

        when(templateRepository.findByCommonCause_Code(code)).thenReturn(List.of(template1, template2));

        List<RefinedScenarioTemplateReadDto> response = service.findByCommonCauseCode(code);

        assertAll(
                () -> assertEquals(2, response.size()),
                () -> assertEquals(template1.getId(), response.getFirst().getId()),
                () -> assertEquals(template1.getTemplate(), response.getFirst().getTemplate()),
                () -> assertEquals(template2.getId(), response.get(1).getId()),
                () -> assertEquals(template2.getTemplate(), response.get(1).getTemplate())
        );

        verify(templateRepository, times(1)).findByCommonCause_Code(code);
    }

    @Test
    @DisplayName("#findAll > When no templates are found > Return empty list")
    void findAllWhenNoTemplatesAreFoundReturnEmptyList() {
        when(templateRepository.findAll()).thenReturn(List.of());

        List<RefinedScenarioTemplateReadDto> response = service.findAll();

        assertTrue(response.isEmpty());
        verify(templateRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("#findAll > When templates are found > Return mapped DTOs")
    void findAllWhenTemplatesAreFoundReturnMappedDtos() {
        RefinedScenarioTemplate template1 = assembleTemplate.apply("CC-1", null);
        RefinedScenarioTemplate template2 = assembleTemplate.apply("CC-2", UCAType.NOT_PROVIDED);

        when(templateRepository.findAll()).thenReturn(List.of(template1, template2));

        List<RefinedScenarioTemplateReadDto> response = service.findAll();

        assertAll(
                () -> assertEquals(2, response.size()),
                () -> assertEquals(template1.getId(), response.getFirst().getId()),
                () -> assertEquals(template2.getId(), response.get(1).getId())
        );

        verify(templateRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("#applyTemplates > When UCA is found > Return templates applicable to UCA type and generic templates")
    void applyTemplatesWhenUcaIsFoundReturnTemplatesApplicableToUcaTypeAndGenericTemplates() {
        UUID ucaId = UUID.randomUUID();
        UnsafeControlActionFullReadDto uca = assembleUca.apply(ucaId);
        RefinedScenarioTemplate specificTemplate = assembleTemplate.apply("CC-1", UCAType.PROVIDED);
        RefinedScenarioTemplate genericTemplate = assembleTemplate.apply("CC-2", null);

        when(step3Client.readUnsafeControlAction(ucaId)).thenReturn(uca);
        when(templateRepository.findByUnsafeControlActionType(UCAType.PROVIDED)).thenReturn(List.of(specificTemplate));
        when(templateRepository.findByUnsafeControlActionTypeIsNull()).thenReturn(List.of(genericTemplate));

        List<RefinedScenarioTemplateReadDto> response = service.applyTemplates(ucaId);

        assertAll(
                () -> assertEquals(2, response.size()),
                () -> assertTrue(response.stream().anyMatch(t -> t.getId().equals(specificTemplate.getId()))),
                () -> assertTrue(response.stream().anyMatch(t -> t.getId().equals(genericTemplate.getId())))
        );

        verify(step3Client, times(1)).readUnsafeControlAction(ucaId);
        verify(templateRepository, times(1)).findByUnsafeControlActionType(UCAType.PROVIDED);
        verify(templateRepository, times(1)).findByUnsafeControlActionTypeIsNull();
    }

    @Test
    @DisplayName("#applyTemplatesByCommonCause > When UCA and common cause are valid > Return filtered templates")
    void applyTemplatesByCommonCauseWhenUcaAndCommonCauseAreValidReturnFilteredTemplates() {
        UUID ucaId = UUID.randomUUID();
        String commonCauseCode = "CC-1";
        UnsafeControlActionFullReadDto uca = assembleUca.apply(ucaId);
        RefinedScenarioTemplate matchingTemplate = assembleTemplate.apply(commonCauseCode, UCAType.PROVIDED);
        RefinedScenarioTemplate nonMatchingTemplate = assembleTemplate.apply(commonCauseCode, UCAType.NOT_PROVIDED);
        RefinedScenarioTemplate genericTemplate = assembleTemplate.apply(commonCauseCode, null);

        when(step3Client.readUnsafeControlAction(ucaId)).thenReturn(uca);
        when(templateRepository.findByCommonCause_Code(commonCauseCode)).thenReturn(List.of(matchingTemplate, nonMatchingTemplate, genericTemplate));

        List<RefinedScenarioTemplateReadDto> response = service.applyTemplatesByCommonCause(ucaId, commonCauseCode);

        assertAll(
                () -> assertEquals(2, response.size()),
                () -> assertTrue(response.stream().anyMatch(t -> t.getId().equals(matchingTemplate.getId()))),
                () -> assertTrue(response.stream().anyMatch(t -> t.getId().equals(genericTemplate.getId()))),
                () -> assertFalse(response.stream().anyMatch(t -> t.getId().equals(nonMatchingTemplate.getId())))
        );

        verify(step3Client, times(1)).readUnsafeControlAction(ucaId);
        verify(templateRepository, times(1)).findByCommonCause_Code(commonCauseCode);
    }

    @Test
    @DisplayName("#applyTemplatesGroupedByCommonCause > When templates are found > Return grouped templates by common cause")
    void applyTemplatesGroupedByCommonCauseWhenTemplatesAreFoundReturnGroupedTemplatesByCommonCause() {
        UUID ucaId = UUID.randomUUID();
        UnsafeControlActionFullReadDto uca = assembleUca.apply(ucaId);
        RefinedScenarioTemplate template1 = assembleTemplate.apply("CC-1", UCAType.PROVIDED);
        RefinedScenarioTemplate template2 = assembleTemplate.apply("CC-2", null);

        when(step3Client.readUnsafeControlAction(ucaId)).thenReturn(uca);
        when(templateRepository.findByUnsafeControlActionType(UCAType.PROVIDED)).thenReturn(List.of(template1));
        when(templateRepository.findByUnsafeControlActionTypeIsNull()).thenReturn(List.of(template2));

        List<RefinedScenarioCommonCauseReadDto> response = service.applyTemplatesGroupedByCommonCause(ucaId);

        assertAll(
                () -> assertEquals(2, response.size()),
                () -> assertTrue(response.stream().anyMatch(cc -> "CC-1".equals(cc.getCode()))),
                () -> assertTrue(response.stream().anyMatch(cc -> "CC-2".equals(cc.getCode())))
        );

        verify(step3Client, times(1)).readUnsafeControlAction(ucaId);
        verify(templateRepository, times(1)).findByUnsafeControlActionType(UCAType.PROVIDED);
        verify(templateRepository, times(1)).findByUnsafeControlActionTypeIsNull();
    }

    private final Function<UUID, UnsafeControlActionFullReadDto> assembleUca = (ucaId) -> UnsafeControlActionFullReadDto.builder()
            .id(ucaId)
            .analysis_id(UUID.randomUUID())
            .name("Test UCA")
            .uca_code("UCA-1")
            .type(UCAType.PROVIDED)
            .build();

    private final BiFunction<String, UCAType, RefinedScenarioTemplate> assembleTemplate = (commonCauseCode, ucaType) -> {
        RefinedScenarioCommonCause commonCause = RefinedScenarioCommonCause.builder()
                .id(UUID.randomUUID())
                .code(commonCauseCode)
                .commonCause("Common Cause " + commonCauseCode)
                .build();

        return RefinedScenarioTemplate.builder()
                .id(UUID.randomUUID())
                .template("Template for " + commonCauseCode)
                .unsafeControlActionType(ucaType)
                .commonCause(commonCause)
                .build();
    };
}
