package microstamp.step1.dto.export;

import lombok.*;
import microstamp.step1.dto.assumption.AssumptionReadDto;
import microstamp.step1.dto.hazard.HazardReadDto;
import microstamp.step1.dto.loss.LossReadDto;
import microstamp.step1.dto.systemgoal.SystemGoalReadDto;
import microstamp.step1.dto.systemsafetyconstraint.SystemSafetyConstraintReadDto;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExportDto {

    private UUID analysisId;

    private List<SystemGoalReadDto> systemGoals;

    private List<AssumptionReadDto> assumptions;

    private List<LossReadDto> losses;

    private List<HazardReadDto> hazards;

    private List<SystemSafetyConstraintReadDto> systemSafetyConstraints;

}
