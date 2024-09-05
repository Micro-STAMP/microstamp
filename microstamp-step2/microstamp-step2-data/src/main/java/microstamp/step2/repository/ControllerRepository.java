package microstamp.step2.repository;

import microstamp.step2.entity.Controller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ControllerRepository extends JpaRepository<microstamp.step2.entity.Controller, UUID> {

    List<Controller> findByAnalysisId(UUID id);

}
