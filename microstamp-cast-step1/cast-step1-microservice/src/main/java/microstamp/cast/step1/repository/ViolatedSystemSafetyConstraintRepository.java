package microstamp.cast.step1.repository;

import microstamp.cast.step1.entity.ViolatedSystemSafetyConstraint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ViolatedSystemSafetyConstraintRepository extends JpaRepository<ViolatedSystemSafetyConstraint, UUID> {

    List<ViolatedSystemSafetyConstraint> findByAnalysisId(UUID analysisId);

    //Aqui também dá pra lançar uma query pra limpar as conexões geradas
}