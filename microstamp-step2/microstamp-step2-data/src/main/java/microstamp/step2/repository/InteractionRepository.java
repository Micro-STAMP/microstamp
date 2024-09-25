package microstamp.step2.repository;

import microstamp.step2.entity.Interaction;
import microstamp.step2.enumeration.InteractionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InteractionRepository extends JpaRepository<Interaction, UUID> {

    List<Interaction> findByConnectionId(UUID id);

    List<Interaction> findByInteractionType(InteractionType interactionType);

}
