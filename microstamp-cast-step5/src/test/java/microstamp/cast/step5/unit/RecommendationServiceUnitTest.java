package microstamp.cast.step5.unit;

import microstamp.cast.step5.client.CastStep3Client;
import microstamp.cast.step5.client.CastStep4Client;
import microstamp.cast.step5.client.ControlActionContributionReadDto;
import microstamp.cast.step5.client.SystemicFactorReadDto;
import microstamp.cast.step5.dto.recommendation.RecommendationReadDto;
import microstamp.cast.step5.dto.recommendation.RecommendationUpdateDto;
import microstamp.cast.step5.entity.Recommendation;
import microstamp.cast.step5.mapper.RecommendationMapper;
import microstamp.cast.step5.repository.RecommendationRepository;
import microstamp.cast.step5.service.AuditLogService;
import microstamp.cast.step5.service.impl.RecommendationServiceImpl;
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
public class RecommendationServiceUnitTest {

    @InjectMocks
    private RecommendationServiceImpl service;

    @Mock
    private RecommendationRepository repository;

    @Spy
    private RecommendationMapper mapper;

    @Mock
    private CastStep3Client castStep3Client;

    @Mock
    private CastStep4Client castStep4Client;

    @Mock
    private AuditLogService auditLogService;

    @Test
    @DisplayName("#update > When the linked CAC had its code changed in Step 3 > Keep the CAC and Systemic Factor links by UUID")
    void updateWhenTheLinkedCacHadItsCodeChangedKeepTheLinksByUuid() {
        UUID recommendationId = UUID.randomUUID();
        UUID analysisId = UUID.randomUUID();
        UUID cacId = UUID.randomUUID();
        UUID systemicFactorId = UUID.randomUUID();
        Recommendation mock = Recommendation.builder()
                .id(recommendationId)
                .analysisId(analysisId)
                .description("Description")
                .controlActionContributionIds(List.of(cacId))
                .systemicFactorIds(List.of(systemicFactorId))
                .build();

        when(repository.findById(recommendationId)).thenReturn(Optional.of(mock));
        when(repository.save(any(Recommendation.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(castStep3Client.readControlActionContribution(cacId)).thenReturn(new ControlActionContributionReadDto(
                cacId, analysisId, UUID.randomUUID(), "CAC-9", "Control Action", "NOT_PROVIDED", null, List.of(), List.of()));
        when(castStep4Client.readSystemicFactor(systemicFactorId)).thenReturn(new SystemicFactorReadDto(
                systemicFactorId, analysisId, "COMMUNICATION_AND_COORDINATION", null, "Description", List.of(cacId)));

        RecommendationReadDto response = service.update(recommendationId, new RecommendationUpdateDto(
                "Updated description", List.of(cacId), List.of(systemicFactorId),
                null, null, null, null, null, null, null, null, null));

        assertAll(
                () -> assertEquals(recommendationId, response.id()),
                () -> assertEquals(analysisId, response.analysisId()),
                () -> assertEquals(List.of(cacId), response.controlActionContributionIds()),
                () -> assertEquals(List.of(systemicFactorId), response.systemicFactorIds()),
                () -> verify(repository, times(1)).save(mock)
        );
    }
}
