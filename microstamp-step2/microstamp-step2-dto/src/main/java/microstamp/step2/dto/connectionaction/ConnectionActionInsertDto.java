package microstamp.step2.dto.connectionaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import microstamp.step2.enumeration.ConnectionActionType;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionActionInsertDto {

    @NotBlank
    private String name;

    @NotBlank
    private String code;

    @NotNull
    private ConnectionActionType connectionActionType;

    @NotNull
    private UUID connectionId;

}
