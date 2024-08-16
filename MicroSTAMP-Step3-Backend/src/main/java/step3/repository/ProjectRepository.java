package step3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import step3.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
