package microstamp.step4new.dto.formalscenario;

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
public class FormalScenarioInsertDto implements Serializable {

    @NotNull
    private UUID unsafeControlActionId;

    @NotNull
    private UUID analysisId;
}
