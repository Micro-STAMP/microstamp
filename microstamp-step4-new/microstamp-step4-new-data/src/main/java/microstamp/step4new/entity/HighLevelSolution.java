package microstamp.step4new.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "HighLevelSolution")
@Table(name = "high_level_solutions", uniqueConstraints = { @UniqueConstraint(columnNames = { "formal_scenario_class_id" }) })
public class HighLevelSolution {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "formal_scenario_class_id", nullable = false, updatable = false, unique = true)
    private FormalScenarioClass formalScenarioClass;

    @Column(length = 5000)
    private String processBehavior;

    @Column(length = 5000)
    private String controllerBehavior;

    @Column(length = 5000)
    private String otherSolutions;
}
