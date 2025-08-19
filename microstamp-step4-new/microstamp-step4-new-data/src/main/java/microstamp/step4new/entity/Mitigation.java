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
@Entity(name = "Mitigation")
@Table(name = "mitigations", uniqueConstraints = { @UniqueConstraint(columnNames = { "refined_scenario_id" }) })
public class Mitigation {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@JdbcTypeCode(Types.VARCHAR)
	private UUID id;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "refined_scenario_id", nullable = false, updatable = false, unique = true)
	private RefinedScenario refinedScenario;

	@Column(name = "mitigation", length = 5000)
	private String mitigation;
}


