package microstamp.step4new.dto.formalscenario;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import microstamp.step4new.dto.formalscenarioclass.FormalScenarioClassReadDto;

import java.io.Serializable;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FormalScenarioReadDto implements Serializable {

    @NotBlank
    private FormalScenarioClassReadDto class1;

    @NotBlank
    private FormalScenarioClassReadDto class2;

    @NotBlank
    private FormalScenarioClassReadDto class3;

    @NotBlank
    private FormalScenarioClassReadDto class4;
}
