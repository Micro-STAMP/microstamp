package step3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import step3.entity.ControlAction;

import java.util.List;

public interface ControlActionRepository extends JpaRepository<ControlAction, Long> {
    List<ControlAction> findByControllerId(Long controllerId);
}
