package microstamp.step4new.dto.refinedscenario;

import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RefinedScenarioCommonCauseReadDto implements Serializable {
	private UUID id;
	private String code;
	private String cause;
	private List<RefinedScenarioTemplateSimpleReadDto> templates;
}