package microstamp.step2.dto.connection;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import microstamp.step2.enumeration.Style;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionInsertDto {

    @NotBlank
    private String code;

    @NotNull
    private UUID sourceId;

    @NotNull
    private UUID targetId;

    private Style style;

    @NotNull
    private UUID analysisId;

}
