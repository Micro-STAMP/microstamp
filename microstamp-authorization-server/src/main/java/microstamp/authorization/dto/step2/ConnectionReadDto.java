package microstamp.authorization.dto.step2;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import microstamp.authorization.dto.step2.enumeration.Style;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionReadDto {

    @NotNull
    private UUID id;

    @NotBlank
    private String code;

    @NotNull
    private ComponentReadDto source;

    @NotNull
    private ComponentReadDto target;

    private Style style;

    private List<ConnectionActionReadDto> connectionActions;

}
