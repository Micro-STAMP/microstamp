package step3.entity.association;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import step3.entity.Context;

import java.sql.Types;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "ContextState")
@Table(name = "context_state")
public class ContextState {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;
    @JdbcTypeCode(Types.VARCHAR)
    private UUID stateId;
    @ManyToOne
    @JoinColumn(name = "context_id")
    private Context context;
}
