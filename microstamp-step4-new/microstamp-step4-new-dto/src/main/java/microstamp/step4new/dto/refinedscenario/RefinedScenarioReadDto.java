package microstamp.step4new.dto.refinedscenario;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RefinedScenarioReadDto implements Serializable {
	private UUID id;
	private UUID commonCauseId;
	private String refinedScenario;
	private UUID formalScenarioClassId;
	private UUID unsafeControlActionId;
}