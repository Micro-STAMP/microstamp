package microstamp.cast.step5.service.impl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import microstamp.cast.step5.client.CastStep3Client;
import microstamp.cast.step5.client.CastStep4Client;
import microstamp.cast.step5.dto.recommendation.RecommendationInsertDto;
import microstamp.cast.step5.dto.recommendation.RecommendationReadDto;
import microstamp.cast.step5.dto.recommendation.RecommendationUpdateDto;
import microstamp.cast.step5.entity.Recommendation;
import microstamp.cast.step5.exception.InadequateControlActionReferenceNotFoundException;
import microstamp.cast.step5.exception.RecommendationNotFoundException;
import microstamp.cast.step5.exception.SystemicFactorReferenceNotFoundException;
import microstamp.cast.step5.mapper.RecommendationMapper;
import microstamp.cast.step5.repository.RecommendationRepository;
import microstamp.cast.step5.service.RecommendationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private final RecommendationRepository repository;
    private final RecommendationMapper mapper;
    private final CastStep3Client castStep3Client;
    private final CastStep4Client castStep4Client;

    @Override
    @Transactional
    public RecommendationReadDto create(RecommendationInsertDto dto) {
        validateInadequateControlActionsExist(dto.inadequateControlActionIds());
        validateSystemicFactorsExist(dto.systemicFactorIds());
        Recommendation entity = mapper.toEntity(dto);
        return mapper.toReadDto(repository.save(entity));
    }

    @Override
    public List<RecommendationReadDto> findByAnalysisId(UUID analysisId) {
        return repository.findByAnalysisId(analysisId).stream()
                .map(mapper::toReadDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RecommendationReadDto update(UUID id, RecommendationUpdateDto dto) {
        Recommendation entity = repository.findById(id)
                .orElseThrow(() -> new RecommendationNotFoundException("Recommendation not found"));
        validateInadequateControlActionsExist(dto.inadequateControlActionIds());
        validateSystemicFactorsExist(dto.systemicFactorIds());
        mapper.updateEntityFromDto(dto, entity);
        return mapper.toReadDto(repository.save(entity));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    private void validateInadequateControlActionsExist(List<UUID> inadequateControlActionIds) {
        if (inadequateControlActionIds == null) {
            return;
        }
        for (UUID icaId : inadequateControlActionIds) {
            try {
                castStep3Client.readInadequateControlAction(icaId);
            } catch (FeignException.NotFound ex) {
                throw new InadequateControlActionReferenceNotFoundException("Inadequate Control Action not found: " + icaId);
            }
        }
    }

    private void validateSystemicFactorsExist(List<UUID> systemicFactorIds) {
        if (systemicFactorIds == null) {
            return;
        }
        for (UUID systemicFactorId : systemicFactorIds) {
            try {
                castStep4Client.readSystemicFactor(systemicFactorId);
            } catch (FeignException.NotFound ex) {
                throw new SystemicFactorReferenceNotFoundException("Systemic Factor not found: " + systemicFactorId);
            }
        }
    }
}
