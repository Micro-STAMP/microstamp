package microstamp.step2.dto.state;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StateUpdateDto {

    @NotBlank
    private String name;

    @NotBlank
    private String code;

}
