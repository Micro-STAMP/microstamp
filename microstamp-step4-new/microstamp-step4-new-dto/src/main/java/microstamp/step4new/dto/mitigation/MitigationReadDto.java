package microstamp.step4new.dto.mitigation;

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
public class MitigationReadDto implements Serializable {

	@NotNull
	private UUID id;

	@NotNull
	private UUID refinedScenarioId;

	@NotBlank
	private String refinedScenarioCode;

	private String mitigation;

	private String code;
}


