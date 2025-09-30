package microstamp.authorization.dto.step4new;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HighLevelSolutionReadDto {

    private UUID id;
    private UUID formalScenarioClassId;
    private String processBehavior;
    private String controllerBehavior;
    private String otherSolutions;
}
