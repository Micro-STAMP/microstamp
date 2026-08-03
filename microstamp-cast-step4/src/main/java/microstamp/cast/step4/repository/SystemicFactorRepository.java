package microstamp.cast.step4.repository;

import microstamp.cast.step4.entity.SystemicFactor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SystemicFactorRepository extends JpaRepository<SystemicFactor, UUID> {
    List<SystemicFactor> findByAnalysisId(UUID analysisId);
}
