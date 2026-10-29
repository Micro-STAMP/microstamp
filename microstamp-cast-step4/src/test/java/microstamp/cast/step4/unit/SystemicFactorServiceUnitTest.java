package microstamp.cast.step4.unit;

import feign.FeignException;
import microstamp.cast.step4.client.CastStep3Client;
import microstamp.cast.step4.client.ControlActionContributionReadDto;
import microstamp.cast.step4.dto.systemicfactor.SystemicFactorReadDto;
import microstamp.cast.step4.dto.systemicfactor.SystemicFactorUpdateDto;
import microstamp.cast.step4.entity.SystemicFactor;
import microstamp.cast.step4.entity.enums.SystemicFactorCategory;
import microstamp.cast.step4.exception.ControlActionContributionReferenceNotFoundException;
import microstamp.cast.step4.mapper.SystemicFactorMapper;
import microstamp.cast.step4.repository.SystemicFactorRepository;
import microstamp.cast.step4.service.AuditLogService;
import microstamp.cast.step4.service.impl.SystemicFactorServiceImpl;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SystemicFactorServiceUnitTest {

    @InjectMocks
    private SystemicFactorServiceImpl service;

    @Mock
    private SystemicFactorRepository repository;

    @Spy
    private SystemicFactorMapper mapper;

    @Mock
    private CastStep3Client castStep3Client;

    @Mock
    private AuditLogService auditLogService;

    @Test
    @DisplayName("#update > When the linked CAC had its code changed in Step 3 > Keep the link by UUID")
    void updateWhenTheLinkedCacHadItsCodeChangedKeepTheLinkByUuid() {
        UUID factorId = UUID.randomUUID();
        UUID analysisId = UUID.randomUUID();
        UUID cacId = UUID.randomUUID();
        SystemicFactor mock = SystemicFactor.builder()
                .id(factorId)
                .analysisId(analysisId)
                .category(SystemicFactorCategory.COMMUNICATION_AND_COORDINATION)
                .description("Description")
                .controlActionContributionIds(List.of(cacId))
                .build();

        when(repository.findById(factorId)).thenReturn(Optional.of(mock));
        when(repository.save(any(SystemicFactor.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(castStep3Client.readControlActionContribution(cacId)).thenReturn(new ControlActionContributionReadDto(
                cacId, analysisId, UUID.randomUUID(), "CAC-9", "Control Action", "NOT_PROVIDED", null, List.of(), List.of()));

        SystemicFactorReadDto response = service.update(factorId, new SystemicFactorUpdateDto(
                SystemicFactorCategory.COMMUNICATION_AND_COORDINATION, null, "Updated description", List.of(cacId)));

        assertAll(
                () -> assertEquals(factorId, response.id()),
                () -> assertEquals(analysisId, response.analysisId()),
                () -> assertEquals(List.of(cacId), response.controlActionContributionIds()),
                () -> verify(repository, times(1)).save(mock)
        );
    }

    @Test
    @DisplayName("#update > When a linked CAC was deleted in Step 3 > Throw an exception without saving")
    void updateWhenALinkedCacWasDeletedThrowAnExceptionWithoutSaving() {
        UUID factorId = UUID.randomUUID();
        UUID orphanCacId = UUID.randomUUID();
        SystemicFactor mock = SystemicFactor.builder()
                .id(factorId)
                .analysisId(UUID.randomUUID())
                .category(SystemicFactorCategory.COMMUNICATION_AND_COORDINATION)
                .description("Description")
                .controlActionContributionIds(List.of(orphanCacId))
                .build();

        when(repository.findById(factorId)).thenReturn(Optional.of(mock));
        when(castStep3Client.readControlActionContribution(orphanCacId)).thenThrow(FeignException.NotFound.class);

        assertThrows(ControlActionContributionReferenceNotFoundException.class, () -> service.update(factorId, new SystemicFactorUpdateDto(
                SystemicFactorCategory.COMMUNICATION_AND_COORDINATION, null, "Description", List.of(orphanCacId))));
        verify(repository, never()).save(any());
    }
}
