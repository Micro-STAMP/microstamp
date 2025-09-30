package microstamp.authorization.dto.step4new;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormalScenarioClassReadDto {

    private UUID id;
    private String output;
    private String input;
    private String code;
}
