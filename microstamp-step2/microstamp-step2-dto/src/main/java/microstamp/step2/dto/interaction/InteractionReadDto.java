package microstamp.step2.dto.interaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import microstamp.step2.enumeration.InteractionType;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InteractionReadDto {

    @NotNull
    private UUID id;

    @NotBlank
    private String name;

    @NotBlank
    private String code;

    @NotNull
    private InteractionType interactionType;

}
