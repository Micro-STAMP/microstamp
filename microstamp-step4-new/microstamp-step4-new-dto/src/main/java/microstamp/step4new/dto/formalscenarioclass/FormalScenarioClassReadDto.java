package microstamp.step4new.dto.formalscenarioclass;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FormalScenarioClassReadDto implements Serializable {

    @NotBlank
    private String output;

    @NotBlank
    private String input;

    @NotBlank
    private String code;
}
