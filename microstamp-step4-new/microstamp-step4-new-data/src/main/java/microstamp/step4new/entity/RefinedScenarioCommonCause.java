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
@Entity(name = "RefinedScenarioCommonCause")
@Table(name = "refined_scenarios_common_causes", uniqueConstraints = { @UniqueConstraint(columnNames = { "code" }) })
public class RefinedScenarioCommonCause {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@JdbcTypeCode(Types.VARCHAR)
	private UUID id;

	@Column(nullable = false, updatable = false)
	private String code;

	@Column(name = "common_cause", nullable = false, length = 300)
	private String commonCause;
}