package microstamp.authorization.repository;

import microstamp.authorization.entity.Analysis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AnalysisRepository extends JpaRepository<Analysis, UUID> {

    List<Analysis> findByUserId(UUID userId);

}
