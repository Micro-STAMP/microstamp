package step3.entity.mit.association;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import step3.entity.mit.UnsafeControlAction;

import java.sql.Types;
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
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;
    @JdbcTypeCode(Types.VARCHAR)
    private UUID stateId;
    @ManyToOne
    @JoinColumn(name = "unsafe_control_action_id")
    private UnsafeControlAction unsafeControlAction;
}
