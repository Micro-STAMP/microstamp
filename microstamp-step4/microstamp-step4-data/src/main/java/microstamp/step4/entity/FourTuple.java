package microstamp.step4.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "FourTuple")
@Table(name = "four_tuples", uniqueConstraints = { @UniqueConstraint(columnNames = { "code", "analysis_id" }) })
public class FourTuple {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    private String code;

    private String scenario;

    private String associatedCausalFactor;

    private String recommendation;

    private String rationale;

    @ElementCollection
    @CollectionTable(name = "four_tuple_unsafe_control_actions", joinColumns = @JoinColumn(name = "four_tuple_id"))
    @Column(name = "unsafe_control_action_id")
    @JdbcTypeCode(Types.VARCHAR)
    private List<UUID> unsafeControlActions;

    @JdbcTypeCode(Types.VARCHAR)
    private UUID analysisId;
}
