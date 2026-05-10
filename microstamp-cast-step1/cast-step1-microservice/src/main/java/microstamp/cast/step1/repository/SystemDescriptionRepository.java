package microstamp.cast.step1.repository;

import microstamp.cast.step1.entity.SystemDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SystemDescriptionRepository extends JpaRepository<SystemDescription, UUID> {

    List<SystemDescription> findByAnalysisId(UUID analysisId);
}