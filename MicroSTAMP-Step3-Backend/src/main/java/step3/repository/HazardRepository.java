package step3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import step3.entity.Hazard;

import java.util.List;

public interface HazardRepository extends JpaRepository<Hazard, Long> {
    List<Hazard> findByProjectId(Long projectId);
}
