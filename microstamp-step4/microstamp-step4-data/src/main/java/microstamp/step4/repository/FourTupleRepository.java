package microstamp.step4.repository;

import microstamp.step4.entity.FourTuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FourTupleRepository extends JpaRepository<FourTuple, UUID> {

    List<FourTuple> findByAnalysisId(UUID id);

    List<FourTuple> findByUnsafeControlActionsIs(UUID id);

}
