package microstamp.authorization.dto.step3;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
//@NoArgsConstructor
@AllArgsConstructor
public class Step3ExportReadDto {
    private UUID analysisId;
    private List<UnsafeControlActionReadDto> unsafeControlActions;
    private List<RuleReadListDto> rules;
}
