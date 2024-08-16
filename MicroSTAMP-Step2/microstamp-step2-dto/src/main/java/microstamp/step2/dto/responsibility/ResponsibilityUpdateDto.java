package microstamp.step2.dto.responsibility;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

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

    @NotBlank
    private UUID systemSafetyConstraintId;

}
