package microstamp.cast.step1.dto.casthazard;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import microstamp.cast.step1.dto.accidentlossevent.AccidentLossEventReadDto;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CastHazardReadDto {

    @NotNull
    private UUID id;

    @NotBlank
    private String code;

    @NotBlank
    private String name;

    private String description;

    private List<AccidentLossEventReadDto> accidentLossEvents;
}