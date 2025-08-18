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
@Entity(name = "RefinedScenario")
@Table(name = "refined_scenarios")
public class RefinedScenario {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@JdbcTypeCode(Types.VARCHAR)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_common_causes", nullable = false)
	private RefinedScenarioCommonCause commonCause;

	@Column(name = "refined_scenario", nullable = false, length = 5000)
	private String refinedScenario;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "formal_scenario_class_id", nullable = false, updatable = false)
	private FormalScenarioClass formalScenarioClass;

	@JdbcTypeCode(Types.VARCHAR)
	@Column(name = "unsafe_control_action_id", nullable = false)
	private UUID unsafeControlActionId;
}