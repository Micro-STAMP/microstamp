package microstamp.cast.step1.repository;

import microstamp.cast.step1.entity.CastHazard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CastHazardRepository extends JpaRepository<CastHazard, UUID> {

    List<CastHazard> findByAnalysisId(UUID analysisId);

    //Depois ver de adicionar umas querys aqui pra limpar as conexões que faz entre as tabelas (hazard com acidentes)
}