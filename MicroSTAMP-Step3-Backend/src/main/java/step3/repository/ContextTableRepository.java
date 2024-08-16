package step3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import step3.entity.ContextTable;

import java.util.List;
import java.util.Optional;

public interface ContextTableRepository extends JpaRepository<ContextTable, Long> {
    Optional<ContextTable> findByControllerId(Long controllerId);
}
