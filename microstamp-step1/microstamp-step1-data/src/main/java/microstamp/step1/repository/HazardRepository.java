package microstamp.step1.repository;

import microstamp.step1.book.HazardBook;
import microstamp.step1.entity.Hazard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
@Repository
public interface HazardRepository extends JpaRepository<Hazard, UUID> {

    List<Hazard> findByAnalysisId(UUID id);

    @Query(value = HazardBook.FIND_HAZARD_CHILDREN, nativeQuery = true)
    List<Hazard> findHazardChildren(String id);

    @Modifying
    @Query(value = HazardBook.DELETE_LOSSES_ASSOCIATION, nativeQuery = true)
    void deleteLossesAssociation(String id);

    @Modifying
    @Query(value = HazardBook.DELETE_SYSTEM_SAFETY_CONSTRAINT_ASSOCIATION, nativeQuery = true)
    void deleteSystemSafetyConstraintsAssociation(String id);

}
