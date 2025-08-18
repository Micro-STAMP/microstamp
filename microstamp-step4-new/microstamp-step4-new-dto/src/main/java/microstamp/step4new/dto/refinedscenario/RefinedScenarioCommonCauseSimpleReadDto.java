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
public class RefinedScenarioCommonCauseSimpleReadDto implements Serializable {
	private UUID id;
	private String code;
	private String cause;
}