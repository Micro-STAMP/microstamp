package microstamp.step4new.dto.refinedscenario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RefinedScenarioUpdateDto implements Serializable {
	@NotNull
	private UUID commonCauseId;

	@NotBlank
	private String refinedScenario;

	@NotNull
	private UUID formalScenarioClassId;
}