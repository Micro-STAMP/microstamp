package microstamp.step2.repository;

import microstamp.step2.entity.Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, UUID> {

    List<Connection> findByAnalysisId(UUID id);

    List<Connection> findBySourceId(UUID id);

    List<Connection> findByTargetId(UUID id);

}
