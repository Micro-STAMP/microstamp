package microstamp.authorization.dto.step2;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import microstamp.authorization.dto.step1.SystemSafetyConstraintReadDto;

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

}
