package microstamp.step1.dto.hazard;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HazardUpdateDto implements Serializable {

    @NotBlank
    private String name;

    @NotBlank
    private String code;

    private List<UUID> lossIds;

    private UUID fatherId;

}