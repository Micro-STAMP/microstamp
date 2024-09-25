package microstamp.step2.dto.interaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import microstamp.step2.enumeration.InteractionType;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InteractionUpdateDto {

    @NotBlank
    private String name;

    @NotBlank
    private String code;

    @NotNull
    private InteractionType interactionType;

}
