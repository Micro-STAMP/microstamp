package microstamp.cast.step1.repository;

import jakarta.transaction.Transactional;
import microstamp.cast.step1.book.AccidentLossEventBook;
import microstamp.cast.step1.entity.AccidentLossEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Transactional
@Repository
public interface AccidentLossEventRepository extends JpaRepository<AccidentLossEvent, UUID> {

    List<AccidentLossEvent> findByAnalysisId(UUID analysisId);

    @Modifying
    @Query(value = AccidentLossEventBook.DELETE_HAZARD_ASSOCIATION, nativeQuery = true)
    void deleteHazardAssociation(String id);

}