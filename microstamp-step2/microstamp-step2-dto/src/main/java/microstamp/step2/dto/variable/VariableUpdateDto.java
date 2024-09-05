package microstamp.step2.dto.variable;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VariableUpdateDto {

    @NotBlank
    private String name;

    @NotBlank
    private String code;

}
