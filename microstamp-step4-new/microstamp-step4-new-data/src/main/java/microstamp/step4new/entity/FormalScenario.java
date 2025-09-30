package microstamp.step4new.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "FormalScenario")
@Table(name = "formal_scenarios", uniqueConstraints = { @UniqueConstraint(columnNames = { "unsafe_control_action_id" }) })
public class FormalScenario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    @Column(name = "unsafe_control_action_id", nullable = false)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID unsafeControlActionId;

    @Column(name = "analysis_id", nullable = false)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID analysisId;

    @OneToMany(mappedBy = "formalScenario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<FormalScenarioClass> classes = new ArrayList<>();

    public void addClass(FormalScenarioClass scenarioClass) {
        scenarioClass.setFormalScenario(this);
        this.classes.add(scenarioClass);
    }
}
