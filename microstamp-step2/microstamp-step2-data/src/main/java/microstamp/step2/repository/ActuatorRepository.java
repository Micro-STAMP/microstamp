package microstamp.step2.repository;

import microstamp.step2.entity.Actuator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ActuatorRepository extends JpaRepository<Actuator, UUID> {

    List<Actuator> findByAnalysisId(UUID id);

}
