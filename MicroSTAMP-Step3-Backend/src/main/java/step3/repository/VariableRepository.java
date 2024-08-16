package step3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import step3.entity.Variable;

import java.util.List;

public interface VariableRepository extends JpaRepository<Variable, Long> {
    List<Variable> findByControllerId(Long controllerId);
}
