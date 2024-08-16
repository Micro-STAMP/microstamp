package microstamp.step2.repository;

import microstamp.step2.entity.ConnectionAction;
import microstamp.step2.enumeration.ConnectionActionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ConnectionActionRepository extends JpaRepository<ConnectionAction, UUID> {

    List<ConnectionAction> findByConnectionId(UUID id);

    List<ConnectionAction> findByConnectionActionType(ConnectionActionType connectionActionType);

}
