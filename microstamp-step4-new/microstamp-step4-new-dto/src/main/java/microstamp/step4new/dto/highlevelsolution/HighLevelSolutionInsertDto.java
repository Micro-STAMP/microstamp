package microstamp.step4new.dto.highlevelsolution;

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
public class HighLevelSolutionInsertDto implements Serializable {

    @NotNull
    private UUID formalScenarioClassId;

    private String processBehavior;

    private String controllerBehavior;

    private String otherSolutions;
}
