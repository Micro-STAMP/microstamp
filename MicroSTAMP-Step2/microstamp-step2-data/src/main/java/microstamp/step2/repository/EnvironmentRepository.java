package microstamp.step2.repository;

import microstamp.step2.entity.Environment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EnvironmentRepository extends JpaRepository<Environment, UUID> {

    Environment findByAnalysisId(UUID id);

}
