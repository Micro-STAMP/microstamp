package microstamp.step2.dto.responsibility;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponsibilityUpdateDto {

    @NotBlank
    private String responsibility;

    @NotBlank
    private String code;

    private UUID systemSafetyConstraintId;

    private List<UUID> violatedSystemSafetyConstraintIds;

}
