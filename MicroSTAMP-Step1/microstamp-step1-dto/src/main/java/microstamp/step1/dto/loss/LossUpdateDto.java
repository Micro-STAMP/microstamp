package microstamp.step1.dto.loss;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LossUpdateDto {

    @NotBlank
    private String name;

    @NotBlank
    private String code;

}
