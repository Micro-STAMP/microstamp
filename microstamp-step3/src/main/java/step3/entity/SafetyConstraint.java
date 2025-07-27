package step3.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.UUID;

@Table(name = "safety_constraint")
@Entity(name = "SafetyConstraint")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SafetyConstraint {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    @Column(name = "safety_constraint_code", unique = true, length = 20)
    private String safetyConstraintCode;

    @OneToOne @JoinColumn(name = "uca_id")
    UnsafeControlAction unsafeControlAction;
}
