package step3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import step3.entity.NotUnsafeControlActionContext;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotUcaContextRepository extends JpaRepository<NotUnsafeControlActionContext, UUID> {
    List<NotUnsafeControlActionContext> findAllByAnalysisId(UUID analysisId);
}
