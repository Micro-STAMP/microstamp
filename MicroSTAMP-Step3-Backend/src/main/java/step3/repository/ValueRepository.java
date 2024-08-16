package step3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import step3.entity.Value;

public interface ValueRepository extends JpaRepository<Value, Long> {
}
