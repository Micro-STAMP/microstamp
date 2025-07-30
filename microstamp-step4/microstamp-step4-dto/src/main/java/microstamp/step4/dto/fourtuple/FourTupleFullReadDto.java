package microstamp.step4.dto.fourtuple;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import microstamp.step3.dto.unsafecontrolaction.UnsafeControlActionReadDto;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FourTupleFullReadDto implements Serializable {

    @NotNull
    private UUID id;

    @NotBlank
    private String scenario;

    @NotBlank
    private String associatedCausalFactor;

    @NotBlank
    private String recommendation;

    @NotBlank
    private String rationale;

    private List<UnsafeControlActionReadDto> unsafeControlActions;

    @NotBlank
    private String code;

}
