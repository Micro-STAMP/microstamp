package microstamp.authorization.dto.step3;

import lombok.*;
import microstamp.authorization.dto.step2.ControlActionReadDto;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnsafeControlActionFullReadDto {

    private UUID id;
    private String name;
    private String uca_code;
    private ControlActionReadDto control_action;
    private String hazard_code;
    private String rule_code;
    private String constraint_code;
}
