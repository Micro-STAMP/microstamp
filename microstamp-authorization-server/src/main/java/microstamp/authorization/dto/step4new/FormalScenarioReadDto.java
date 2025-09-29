package microstamp.authorization.dto.step4new;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormalScenarioReadDto {

    private FormalScenarioClassReadDto class1;
    private FormalScenarioClassReadDto class2;
    private FormalScenarioClassReadDto class3;
    private FormalScenarioClassReadDto class4;
}
