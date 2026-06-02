package microstamp.cast.step1.repository;

import jakarta.transaction.Transactional;
import microstamp.cast.step1.book.CastHazardBook;
import microstamp.cast.step1.entity.CastHazard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Transactional
@Repository
public interface CastHazardRepository extends JpaRepository<CastHazard, UUID> {

    List<CastHazard> findByAnalysisId(UUID analysisId);

    @Modifying
    @Query(value = CastHazardBook.DELETE_ACCIDENT_LOSS_EVENT_ASSOCIATION, nativeQuery = true)
    void deleteAccidentLossEventAssociation(String id);

    @Modifying
    @Query(value = CastHazardBook.DELETE_VIOLATED_CONSTRAINT_ASSOCIATION, nativeQuery = true)
    void deleteViolatedConstraintAssociation(String id);
}