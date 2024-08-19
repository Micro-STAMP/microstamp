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

    public ComponentInsertDto(ComponentBaseInsertDto componentBaseInsertDto, ComponentType type) {
        super(componentBaseInsertDto.getName(), componentBaseInsertDto.getCode(), componentBaseInsertDto.getIsVisible(), componentBaseInsertDto.getFatherId(), componentBaseInsertDto.getBorder(), componentBaseInsertDto.getAnalysisId());
        this.type = type;
    }
}
