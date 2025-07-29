package microstamp.authorization.dto.step4;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Step4ExportReadDto {

    private List<FourTupleFullReadDto> fourTuples;

    private List<UnsafeControlActionFullReadDto> unsafeControlActions;
}
