package microstamp.cast.step5.service;

import microstamp.cast.step5.dto.recommendation.RecommendationInsertDto;
import microstamp.cast.step5.dto.recommendation.RecommendationReadDto;
import microstamp.cast.step5.dto.recommendation.RecommendationUpdateDto;

import java.util.List;
import java.util.UUID;

public interface RecommendationService {
    RecommendationReadDto create(RecommendationInsertDto dto);
    List<RecommendationReadDto> findByAnalysisId(UUID analysisId);
    RecommendationReadDto update(UUID id, RecommendationUpdateDto dto);
    void delete(UUID id);
}
