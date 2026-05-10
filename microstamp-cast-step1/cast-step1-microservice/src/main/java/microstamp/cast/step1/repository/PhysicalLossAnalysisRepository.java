package microstamp.cast.step1.repository;

import microstamp.cast.step1.entity.PhysicalLossAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PhysicalLossAnalysisRepository extends JpaRepository<PhysicalLossAnalysis, UUID> {

    List<PhysicalLossAnalysis> findByAnalysisId(UUID analysisId);
}