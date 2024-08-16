package step3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import step3.entity.Controller;

import java.util.List;

public interface ControllerRepository extends JpaRepository<Controller, Long> {
    List<Controller> findByProjectId(Long projectId);
}
