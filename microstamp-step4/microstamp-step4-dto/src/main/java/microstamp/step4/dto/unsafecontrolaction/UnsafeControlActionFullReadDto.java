package microstamp.step4.dto.unsafecontrolaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import microstamp.step2.dto.state.StateReadDto;
import microstamp.step4.dto.fourtuple.FourTupleReadDto;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnsafeControlActionFullReadDto {

    @NotNull
    private UUID id;

    @NotNull
    private UUID analysis_id;

    @NotBlank
    private String name;

    @NotBlank
    private String uca_code;

    private String hazard_code;

    private String rule_code;

    private List<StateReadDto> states;

    private String type;

    private String constraintName;

    private List<FourTupleReadDto> fourTuples;
}