package microstamp.step4.dto.fourtuple;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class FourTupleInsertDto implements Serializable {

    @NotBlank
    private String scenario;

    @NotBlank
    private String associatedCausalFactor;

    @NotBlank
    private String recommendation;

    @NotBlank
    private String rationale;

    private List<UUID> unsafeControlActionIds;

    @NotBlank
    private String code;

    @NotNull
    private UUID analysisId;

}
