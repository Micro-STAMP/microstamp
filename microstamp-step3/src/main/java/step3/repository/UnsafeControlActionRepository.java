package step3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import step3.book.UnsafeControlActionBook;
import step3.entity.UnsafeControlAction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UnsafeControlActionRepository extends JpaRepository<UnsafeControlAction, UUID> {
    List<UnsafeControlAction> findByControlActionId(UUID id);
    List<UnsafeControlAction> findByRuleCodeAndAnalysisId(String code, UUID analysisId);
    List<UnsafeControlAction> findByAnalysisId(UUID id);
    void deleteByControlActionId(UUID id);
    @Query(UnsafeControlActionBook.FIND_MAX_UCA_CODE_NUMBER_BY_ANALYSIS_ID)
    Integer findMaxUcaCodeNumberByAnalysisId(@Param("analysisId") UUID analysisId);
    Optional<UnsafeControlAction> findByUcaCode(String ucaCode);
}
