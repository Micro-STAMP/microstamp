package microstamp.cast.step5.service.impl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import microstamp.cast.step5.client.CastStep3Client;
import microstamp.cast.step5.client.CastStep4Client;
import microstamp.cast.step5.client.InadequateControlActionReadDto;
import microstamp.cast.step5.client.SystemicFactorReadDto;
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
        validateIcasBelongToAnalysis(dto.inadequateControlActionIds(), dto.analysisId());
        validateSystemicFactorsBelongToAnalysis(dto.systemicFactorIds(), dto.analysisId());
        Recommendation entity = mapper.toEntity(dto);
        return mapper.toReadDto(repository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecommendationReadDto> findByAnalysisId(UUID analysisId) {
        return repository.findByAnalysisId(analysisId).stream()
                .map(mapper::toReadDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public RecommendationReadDto findById(UUID id) {
        Recommendation entity = repository.findById(id)
                .orElseThrow(() -> new RecommendationNotFoundException("Recommendation not found: " + id));
        return mapper.toReadDto(entity);
    }

    @Override
    @Transactional
    public RecommendationReadDto update(UUID id, RecommendationUpdateDto dto) {
        Recommendation entity = repository.findById(id)
                .orElseThrow(() -> new RecommendationNotFoundException("Recommendation not found"));
        validateIcasBelongToAnalysis(dto.inadequateControlActionIds(), entity.getAnalysisId());
        validateSystemicFactorsBelongToAnalysis(dto.systemicFactorIds(), entity.getAnalysisId());
        mapper.updateEntityFromDto(dto, entity);
        return mapper.toReadDto(repository.save(entity));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    private void validateIcasBelongToAnalysis(List<UUID> icaIds, UUID expectedAnalysisId) {
        if (icaIds == null || icaIds.isEmpty()) return;
        for (UUID icaId : icaIds) {
            InadequateControlActionReadDto ica;
            try {
                ica = castStep3Client.readInadequateControlAction(icaId);
            } catch (FeignException.NotFound ex) {
                throw new InadequateControlActionReferenceNotFoundException(
                        "InadequateControlAction not found: " + icaId);
            }
            if (!expectedAnalysisId.equals(ica.analysisId())) {
                throw new InadequateControlActionReferenceNotFoundException(
                        "InadequateControlAction " + icaId + " belongs to analysis " + ica.analysisId() + " and not to analysis " + expectedAnalysisId);
            }
        }
    }

    private void validateSystemicFactorsBelongToAnalysis(List<UUID> sfIds, UUID expectedAnalysisId) {
        if (sfIds == null || sfIds.isEmpty()) return;
        for (UUID sfId : sfIds) {
            SystemicFactorReadDto sf;
            try {
                sf = castStep4Client.readSystemicFactor(sfId);
            } catch (FeignException.NotFound ex) {
                throw new SystemicFactorReferenceNotFoundException(
                        "SystemicFactor não encontrado: " + sfId);
            }
            if (!expectedAnalysisId.equals(sf.analysisId())) {
                throw new SystemicFactorReferenceNotFoundException(
                        "SystemicFactor " + sfId + " belongs to analysis " + sf.analysisId() + " and not to analysis " + expectedAnalysisId);
            }
        }
    }
}
