package microstamp.step2.repository;

import microstamp.step2.entity.Responsibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ResponsibilityRepository extends JpaRepository<Responsibility, UUID> {
}
