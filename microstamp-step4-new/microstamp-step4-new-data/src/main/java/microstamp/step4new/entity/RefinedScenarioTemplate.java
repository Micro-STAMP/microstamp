package microstamp.step4new.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import microstamp.step3.dto.UCAType;

import java.sql.Types;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "RefinedScenarioTemplate")
@Table(name = "refined_scenarios_templates")
public class RefinedScenarioTemplate {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@JdbcTypeCode(Types.VARCHAR)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_common_causes", nullable = false, updatable = false)
	private RefinedScenarioCommonCause commonCause;

	@Column(name = "template", nullable = false, length = 5000)
	private String template;


	@Enumerated(EnumType.STRING)
	@Column(name = "unsafe_control_action_type")
	private UCAType unsafeControlActionType;
}