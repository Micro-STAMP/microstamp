package microstamp.authorization.dto.step4new;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Step4NewExportReadDto {

    private UUID analysisId;

    private List<FormalScenarioReadDto> highLevelScenarios;

    private List<HighLevelSolutionReadDto> highLevelSolution;

    private List<RefinedScenarioReadDto> refinedScenarios;

    private List<MitigationReadDto> refinedSolutions;
}
