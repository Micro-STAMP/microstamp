package microstamp.cast.step1.repository;

import microstamp.cast.step1.entity.AccidentLossEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccidentLossEventRepository extends JpaRepository<AccidentLossEvent, UUID> {

    List<AccidentLossEvent> findByAnalysisId(UUID analysisId);

    //Aqui também dá pra adicionar uma query pra limpar as conexões geradas
}