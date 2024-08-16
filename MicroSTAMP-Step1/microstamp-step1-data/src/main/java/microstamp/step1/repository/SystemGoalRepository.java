package microstamp.step1.repository;

import microstamp.step1.entity.SystemGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SystemGoalRepository extends JpaRepository<SystemGoal, UUID> {

    List<SystemGoal> findByAnalysisId(UUID id);

}
