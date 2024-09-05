package microstamp.step2.dto.connectionaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import microstamp.step2.enumeration.ConnectionActionType;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionActionUpdateDto {

    @NotBlank
    private String name;

    @NotBlank
    private String code;

    @NotNull
    private ConnectionActionType connectionActionType;

}
