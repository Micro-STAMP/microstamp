package microstamp.cast.step3.unit;

import microstamp.cast.step3.dto.controlactioncontribution.ControlActionContributionReadDto;
import microstamp.cast.step3.dto.controlactioncontribution.ControlActionContributionUpdateDto;
import microstamp.cast.step3.entity.ControlActionContribution;
import microstamp.cast.step3.entity.enums.IcaType;
import microstamp.cast.step3.exception.ControlActionContributionNotFoundException;
import microstamp.cast.step3.mapper.ControlActionContributionMapper;
import microstamp.cast.step3.repository.ControlActionContributionRepository;
import microstamp.cast.step3.service.AuditLogService;
import microstamp.cast.step3.service.impl.ControlActionContributionServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ControlActionContributionServiceUnitTest {

    @InjectMocks
    private ControlActionContributionServiceImpl service;

    @Mock
    private ControlActionContributionRepository repository;

    @Spy
    private ControlActionContributionMapper mapper;

    @Mock
    private AuditLogService auditLogService;

    @Test
    @DisplayName("#update > When the CAC is not found > Throw an exception")
    void updateWhenTheCacIsNotFoundThrowAnException() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ControlActionContributionNotFoundException.class, () -> service.update(id, assembleUpdateDto.get()));
    }

    @Test
    @DisplayName("#update > When only the code changes > Keep the id, analysis and component references")
    void updateWhenOnlyTheCodeChangesKeepTheIdAnalysisAndComponentReferences() {
        ControlActionContribution mock = assembleCac.get();
        UUID id = mock.getId();
        UUID analysisId = mock.getAnalysisId();
        UUID componentId = mock.getComponentId();

        when(repository.findById(id)).thenReturn(Optional.of(mock));
        when(repository.save(any(ControlActionContribution.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ControlActionContributionReadDto response = service.update(id, assembleUpdateDto.get());

        assertAll(
                () -> assertEquals("CAC-9", response.code()),
                () -> assertEquals(id, response.id()),
                () -> assertEquals(analysisId, response.analysisId()),
                () -> assertEquals(componentId, response.componentId()),
                () -> verify(repository, times(1)).save(mock)
        );
    }

    private final Supplier<ControlActionContribution> assembleCac = () -> ControlActionContribution.builder()
            .id(UUID.randomUUID())
            .analysisId(UUID.randomUUID())
            .componentId(UUID.randomUUID())
            .code("CAC-1")
            .controlActionName("Control Action")
            .type(IcaType.NOT_PROVIDED)
            .context(List.of("Context"))
            .processModelFlaw(List.of("Process model flaw"))
            .build();

    private final Supplier<ControlActionContributionUpdateDto> assembleUpdateDto = () -> new ControlActionContributionUpdateDto(
            "CAC-9",
            "Control Action",
            IcaType.NOT_PROVIDED,
            null,
            List.of("Context"),
            List.of("Process model flaw"),
            List.of()
    );
}
