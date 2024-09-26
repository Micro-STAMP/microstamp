package step3.entity.association;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import step3.entity.NotUnsafeControlActionContext;

import java.sql.Types;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "NotUnsafeControlActionContextState")
@Table(name = "not_unsafe_control_action_context_state")
public class NotUcaContextState {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;
    @JdbcTypeCode(Types.VARCHAR)
    private UUID stateId;
    @ManyToOne
    @JoinColumn(name = "not_unsafe_control_action_context_id")
    private NotUnsafeControlActionContext notUcaContext;
}
