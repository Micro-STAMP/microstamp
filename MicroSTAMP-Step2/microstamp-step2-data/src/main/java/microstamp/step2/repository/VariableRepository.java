package microstamp.step2.repository;

import microstamp.step2.entity.Variable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VariableRepository extends JpaRepository<Variable, UUID> {

    List<Variable> findByComponentId(UUID id);

}
