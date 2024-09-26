package step3.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import step3.entity.association.NotUcaContextState;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "not_unsafe_control_action_context")
@Entity(name = "NotUnsafeControlActionContext")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotUnsafeControlActionContext {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    @JdbcTypeCode(Types.VARCHAR)
    private UUID controlActionId;

    @OneToMany(
            mappedBy = "notUcaContext",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<NotUcaContextState> stateAssociations = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private UCAType type;

    @JdbcTypeCode(Types.VARCHAR)
    private UUID analysisId;
}
