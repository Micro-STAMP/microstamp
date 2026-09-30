package microstamp.step2.dto.responsibility;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponsibilityInsertDto {

    @NotBlank
    private String responsibility;

    @NotBlank
    private String code;

    @NotNull
    private UUID componentId;

    private UUID systemSafetyConstraintId;

    private List<UUID> violatedSystemSafetyConstraintIds;

}
