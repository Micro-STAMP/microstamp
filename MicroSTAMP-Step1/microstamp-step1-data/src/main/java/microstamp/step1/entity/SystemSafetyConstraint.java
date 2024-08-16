package microstamp.step1.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.io.Serializable;
import java.sql.Types;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "SystemSafetyConstraint")
@Table(name = "system_safety_constraints", uniqueConstraints = { @UniqueConstraint(columnNames = { "code", "analysis_id" }) })
public class SystemSafetyConstraint implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    private String name;

    private String code;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "system_safety_constraint_hazard",
            joinColumns = @JoinColumn(name = "system_safety_constraint_id"),
            inverseJoinColumns = @JoinColumn(name = "hazard_id")
    )
    private List<Hazard> hazardEntities;

    @JdbcTypeCode(Types.VARCHAR)
    private UUID analysisId;

}
