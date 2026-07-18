package microstamp.cast.step3.repository;

import microstamp.cast.step3.entity.InadequateControlAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface InadequateControlActionRepository extends JpaRepository<InadequateControlAction, UUID> {
    List<InadequateControlAction> findByAnalysisId(UUID analysisId);
}