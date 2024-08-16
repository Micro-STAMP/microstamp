package microstamp.step1.repository;

import microstamp.step1.book.LossBook;
import microstamp.step1.entity.Loss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
@Repository
public interface LossRepository extends JpaRepository<Loss, UUID> {

    List<Loss> findByAnalysisId(UUID id);

    @Modifying
    @Query(value = LossBook.DELETE_HAZARDS_ASSOCIATION, nativeQuery = true)
    void deleteHazardsAssociation(String id);
}
