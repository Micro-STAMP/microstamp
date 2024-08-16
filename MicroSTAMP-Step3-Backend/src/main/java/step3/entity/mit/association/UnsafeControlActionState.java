package step3.entity.mit.association;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "UnsafeControlActionState")
@Table(name = "unsafe_control_action_state")
public class UnsafeControlActionState {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID unsafeControlActionId;
    private UUID stateId;
}
