package microstamp.step1.repository;

import microstamp.step1.entity.Assumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AssumptionRepository extends JpaRepository<Assumption, UUID> {

    List<Assumption> findByAnalysisId(UUID id);

}
