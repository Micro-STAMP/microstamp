package microstamp.step2.dto.responsibility;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import microstamp.cast.step1.dto.violatedsystemsafetyconstraint.ViolatedSystemSafetyConstraintReadDto;
import microstamp.step1.dto.systemsafetyconstraint.SystemSafetyConstraintReadDto;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponsibilityReadDto {

    @NotNull
    private UUID id;

    @NotBlank
    private String responsibility;

    @NotBlank
    private String code;

    private SystemSafetyConstraintReadDto systemSafetyConstraint;

    private List<ViolatedSystemSafetyConstraintReadDto> violatedSystemSafetyConstraints;

}
