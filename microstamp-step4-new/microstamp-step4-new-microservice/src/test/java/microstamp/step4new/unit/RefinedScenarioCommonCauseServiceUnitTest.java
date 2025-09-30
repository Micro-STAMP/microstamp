package microstamp.step4new.unit;

import microstamp.step4new.dto.refinedscenario.RefinedScenarioCommonCauseReadDto;
import microstamp.step4new.entity.RefinedScenarioCommonCause;
import microstamp.step4new.entity.RefinedScenarioTemplate;
import microstamp.step4new.exception.Step4NewNotFoundException;
import microstamp.step4new.repository.RefinedScenarioCommonCauseRepository;
import microstamp.step4new.repository.RefinedScenarioTemplateRepository;
import microstamp.step4new.service.impl.RefinedScenarioCommonCauseServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RefinedScenarioCommonCauseServiceUnitTest {

    @InjectMocks
    private RefinedScenarioCommonCauseServiceImpl service;

    @Mock
    private RefinedScenarioCommonCauseRepository commonCauseRepository;

    @Mock
    private RefinedScenarioTemplateRepository templateRepository;

    @Test
    @DisplayName("#findAll > When no common causes are found > Return empty list")
    void findAllWhenNoCommonCausesAreFoundReturnEmptyList() {
        when(commonCauseRepository.findAll()).thenReturn(List.of());

        List<RefinedScenarioCommonCauseReadDto> response = service.findAll();

        assertTrue(response.isEmpty());
        verify(commonCauseRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("#findAll > When common causes are found > Return mapped DTOs sorted by code")
    void findAllWhenCommonCausesAreFoundReturnMappedDtosSortedByCode() {
        RefinedScenarioCommonCause cause1 = assembleCommonCause.apply("CC-2");
        RefinedScenarioCommonCause cause2 = assembleCommonCause.apply("CC-1");
        RefinedScenarioTemplate template1 = assembleTemplate.apply(cause1.getId());
        RefinedScenarioTemplate template2 = assembleTemplate.apply(cause2.getId());

        when(commonCauseRepository.findAll()).thenReturn(List.of(cause1, cause2));
        when(templateRepository.findByCommonCause_Id(cause1.getId())).thenReturn(List.of(template1));
        when(templateRepository.findByCommonCause_Id(cause2.getId())).thenReturn(List.of(template2));

        List<RefinedScenarioCommonCauseReadDto> response = service.findAll();

        assertAll(
                () -> assertEquals(2, response.size()),
                () -> assertEquals("CC-1", response.getFirst().getCode()),
                () -> assertEquals("CC-2", response.get(1).getCode()),
                () -> assertEquals(1, response.getFirst().getTemplates().size()),
                () -> assertEquals(1, response.get(1).getTemplates().size())
        );

        verify(commonCauseRepository, times(1)).findAll();
        verify(templateRepository, times(1)).findByCommonCause_Id(cause1.getId());
        verify(templateRepository, times(1)).findByCommonCause_Id(cause2.getId());
    }

    @Test
    @DisplayName("#findByCode > When common cause is not found > Throw an exception")
    void findByCodeWhenCommonCauseIsNotFoundThrowAnException() {
        String code = "CC-1";

        when(commonCauseRepository.findByCode(code)).thenReturn(Optional.empty());

        assertThrows(Step4NewNotFoundException.class, () -> service.findByCode(code));
        
        verify(commonCauseRepository, times(1)).findByCode(code);
        verify(templateRepository, never()).findByCommonCause_Id(any());
    }

    @Test
    @DisplayName("#findByCode > When common cause is found > Return mapped DTO with templates")
    void findByCodeWhenCommonCauseIsFoundReturnMappedDtoWithTemplates() {
        String code = "CC-1";
        RefinedScenarioCommonCause cause = assembleCommonCause.apply(code);
        RefinedScenarioTemplate template = assembleTemplate.apply(cause.getId());

        when(commonCauseRepository.findByCode(code)).thenReturn(Optional.of(cause));
        when(templateRepository.findByCommonCause_Id(cause.getId())).thenReturn(List.of(template));

        RefinedScenarioCommonCauseReadDto response = service.findByCode(code);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(cause.getCode(), response.getCode()),
                () -> assertEquals(cause.getCommonCause(), response.getCause()),
                () -> assertEquals(1, response.getTemplates().size()),
                () -> assertEquals(template.getId(), response.getTemplates().getFirst().getId()),
                () -> assertEquals(template.getTemplate(), response.getTemplates().getFirst().getTemplate())
        );

        verify(commonCauseRepository, times(1)).findByCode(code);
        verify(templateRepository, times(1)).findByCommonCause_Id(cause.getId());
    }

    @Test
    @DisplayName("#findByCode > When common cause has no templates > Return DTO with empty template list")
    void findByCodeWhenCommonCauseHasNoTemplatesReturnDtoWithEmptyTemplateList() {
        String code = "CC-1";
        RefinedScenarioCommonCause cause = assembleCommonCause.apply(code);

        when(commonCauseRepository.findByCode(code)).thenReturn(Optional.of(cause));
        when(templateRepository.findByCommonCause_Id(cause.getId())).thenReturn(List.of());

        RefinedScenarioCommonCauseReadDto response = service.findByCode(code);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(cause.getCode(), response.getCode()),
                () -> assertTrue(response.getTemplates().isEmpty())
        );

        verify(commonCauseRepository, times(1)).findByCode(code);
        verify(templateRepository, times(1)).findByCommonCause_Id(cause.getId());
    }

    private final Function<String, RefinedScenarioCommonCause> assembleCommonCause = (code) -> RefinedScenarioCommonCause.builder()
            .id(UUID.randomUUID())
            .code(code)
            .commonCause("Common Cause " + code)
            .build();

    private final Function<UUID, RefinedScenarioTemplate> assembleTemplate = (commonCauseId) -> RefinedScenarioTemplate.builder()
            .id(UUID.randomUUID())
            .template("Test template")
            .commonCause(RefinedScenarioCommonCause.builder().id(commonCauseId).build())
            .build();
}
