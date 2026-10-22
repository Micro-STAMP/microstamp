package microstamp.cast.step1.repository;

import microstamp.cast.step1.entity.QuestionRaised;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface QuestionRaisedRepository extends JpaRepository<QuestionRaised, UUID> {

    List<QuestionRaised> findByAnalysisId(UUID analysisId);

    List<QuestionRaised> findByAnalysisIdAndCastStep(UUID analysisId, Integer castStep);
}
