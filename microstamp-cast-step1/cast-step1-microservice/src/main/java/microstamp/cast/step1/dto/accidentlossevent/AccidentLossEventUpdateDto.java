package microstamp.cast.step1.dto.accidentlossevent;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AccidentLossEventUpdateDto {

    @NotBlank
    private String code;

    @NotBlank
    private String name;

    private String description;
}