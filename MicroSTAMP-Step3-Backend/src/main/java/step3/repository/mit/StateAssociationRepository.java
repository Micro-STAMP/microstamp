package step3.repository.mit;

import org.springframework.data.jpa.repository.JpaRepository;
import step3.entity.mit.association.UnsafeControlActionState;

import java.util.List;
import java.util.UUID;

public interface StateAssociationRepository extends JpaRepository<UnsafeControlActionState, UUID> {
    void deleteAllByUnsafeControlActionId(UUID unsafeControlActionId);
    List<UnsafeControlActionState> findAllByUnsafeControlActionId(UUID unsafeControlActionId);

}
