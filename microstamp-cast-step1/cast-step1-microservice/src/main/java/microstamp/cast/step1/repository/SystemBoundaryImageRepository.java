package microstamp.cast.step1.repository;

import microstamp.cast.step1.entity.SystemBoundaryImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SystemBoundaryImageRepository extends JpaRepository<SystemBoundaryImage, UUID> {

    List<SystemBoundaryImage> findByAnalysisId(UUID id);

}
