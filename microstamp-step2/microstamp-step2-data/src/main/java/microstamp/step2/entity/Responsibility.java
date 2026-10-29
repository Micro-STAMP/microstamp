package microstamp.step2.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Types;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Responsibility")
@EntityListeners(AuditingEntityListener.class)
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
    @JdbcTypeCode(Types.VARCHAR)
    private List<UUID> violatedSystemSafetyConstraintIds;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

}
