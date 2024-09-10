package microstamp.authorization.dto.step1;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Step1ExportReadDto {

    private List<SystemGoalReadDto> systemGoals;

    private List<AssumptionReadDto> assumptions;

    private List<LossReadDto> losses;

    private List<HazardReadDto> hazards;

    private List<SystemSafetyConstraintReadDto> systemSafetyConstraints;

}
