package microstamp.authorization.repository;

import microstamp.authorization.entity.Analysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface AnalysisRepository extends JpaRepository<Analysis, UUID> {

    List<Analysis> findByUserId(UUID userId);

    @Query(value = "SELECT * FROM analyses c WHERE c.user_id = '2e776941-de10-4aca-98bb-5dabac287229'", nativeQuery = true)
    List<Analysis> findGuestAnalyses();
}
