package microstamp.step2.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Responsibility")
@Table(name = "responsibilities", uniqueConstraints = { @UniqueConstraint(columnNames = { "code", "component_id" }) })
public class Responsibility {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    private String code;

    private String responsibility;

    @ManyToOne
    @JoinColumn(name = "component_id")
    private Component component;

    @JdbcTypeCode(Types.VARCHAR)
    private UUID systemSafetyConstraintId;

    @ElementCollection
    @CollectionTable(name = "responsibility_violated_constraints", joinColumns = @JoinColumn(name = "responsibility_id"))
    @Column(name = "violated_system_safety_constraint_id")
    private List<UUID> violatedSystemSafetyConstraintIds;

}
