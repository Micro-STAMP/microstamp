package microstamp.authorization.dto.step4new;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefinedScenarioReadDto {

    private UUID id;
    private UUID commonCauseId;
    private String refinedScenario;
    private UUID formalScenarioClassId;
    private UUID unsafeControlActionId;
    private String code;
}
