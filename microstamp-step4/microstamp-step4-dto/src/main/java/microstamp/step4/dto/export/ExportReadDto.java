package microstamp.step4.dto.export;

import lombok.*;
import microstamp.step4.dto.fourtuple.FourTupleFullReadDto;
import microstamp.step4.dto.unsafecontrolcation.UnsafeControlActionFullReadDto;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExportReadDto {

    private UUID analysisId;

    private List<FourTupleFullReadDto> fourTuples;

    private List<UnsafeControlActionFullReadDto> unsafeControlActions;

}
