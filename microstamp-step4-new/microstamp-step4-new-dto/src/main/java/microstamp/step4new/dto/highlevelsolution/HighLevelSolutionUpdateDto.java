package microstamp.step4new.dto.highlevelsolution;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HighLevelSolutionUpdateDto implements Serializable {

    private String processBehavior;

    private String controllerBehavior;

    private String otherSolutions;
}
