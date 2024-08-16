package microstamp.step2.dto.state;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StateInsertDto {

    @NotBlank
    private String name;

    @NotBlank
    private String code;

    @NotNull
    private UUID variableId;

}
