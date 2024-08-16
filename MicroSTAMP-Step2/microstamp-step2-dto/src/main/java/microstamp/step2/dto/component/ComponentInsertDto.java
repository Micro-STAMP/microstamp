package microstamp.step2.dto.component;

import lombok.*;
import microstamp.step2.enumeration.ComponentType;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComponentInsertDto extends ComponentBaseInsertDto{

    private ComponentType type;

}
