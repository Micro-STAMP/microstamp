package microstamp.step1.dto.systemgoal;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SystemGoalUpdateDto {

    @NotBlank
    private String name;

    @NotBlank
    private String code;

}
