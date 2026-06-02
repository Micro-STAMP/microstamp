package microstamp.cast.step1.repository;

import microstamp.cast.step1.book.ViolatedSystemSafetyConstraintBook;
import microstamp.cast.step1.entity.ViolatedSystemSafetyConstraint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ViolatedSystemSafetyConstraintRepository extends JpaRepository<ViolatedSystemSafetyConstraint, UUID> {

    List<ViolatedSystemSafetyConstraint> findByAnalysisId(UUID analysisId);

    @Modifying
    @Query(value = ViolatedSystemSafetyConstraintBook.DELETE_HAZARD_ASSOCIATION, nativeQuery = true)
    void deleteHazardAssociation(String id);

}