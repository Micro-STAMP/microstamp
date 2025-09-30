package microstamp.authorization.dto.step4new;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MitigationReadDto {

    private UUID id;
    private UUID refinedScenarioId;
    private String refinedScenarioCode;
    private String mitigation;
    private String code;
}
