package microstamp.step4new.dto.formalscenarioclass;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FormalScenarioClassReadDto implements Serializable {

    @NotNull
    private UUID id;

    @NotBlank
    private String output;

    @NotBlank
    private String input;

    @NotBlank
    private String code;
}
