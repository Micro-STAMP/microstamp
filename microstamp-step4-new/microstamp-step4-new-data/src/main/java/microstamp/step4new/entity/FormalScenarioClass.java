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
@Entity(name = "FormalScenarioClass")
@Table(name = "formal_scenario_classes")
public class FormalScenarioClass {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formal_scenario_id", nullable = false)
    private FormalScenario formalScenario;

    @Column(nullable = false, updatable = false)
    private String code;

}