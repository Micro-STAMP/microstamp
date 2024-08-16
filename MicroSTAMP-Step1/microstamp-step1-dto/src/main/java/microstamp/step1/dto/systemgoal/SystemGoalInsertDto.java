package microstamp.step1.dto.systemgoal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SystemGoalInsertDto {

    @NotBlank
    private String name;

    @NotBlank
    private String code;

    @NotNull
    private UUID analysisId;

}
