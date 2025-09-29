package microstamp.step4new.dto.export;

import lombok.*;
import microstamp.step4new.dto.formalscenario.FormalScenarioReadDto;
import microstamp.step4new.dto.highlevelsolution.HighLevelSolutionReadDto;
import microstamp.step4new.dto.mitigation.MitigationReadDto;
import microstamp.step4new.dto.refinedscenario.RefinedScenarioReadDto;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExportReadDto {

    private UUID analysisId;

    private List<FormalScenarioReadDto> highLevelScenarios;

    private List<HighLevelSolutionReadDto> highLevelSolution;

    private List<RefinedScenarioReadDto> refinedScenarios;

    private List<MitigationReadDto> refinedSolutions;

}
