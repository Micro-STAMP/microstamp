package microstamp.step2.dto.controlaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import microstamp.step2.dto.connection.ConnectionReadDto;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ControlActionReadDto {

    @NotNull
    private UUID id;

    @NotBlank
    private String name;

    @NotBlank
    private String code;

    @NotNull
    private ConnectionReadDto connection;

}
