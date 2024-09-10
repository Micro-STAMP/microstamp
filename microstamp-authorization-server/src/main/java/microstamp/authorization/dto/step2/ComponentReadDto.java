package microstamp.authorization.dto.step2;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import microstamp.authorization.dto.step2.enumeration.Style;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComponentReadDto {

    @NotNull
    private UUID id;

    @NotBlank
    private String name;

    @NotBlank
    private String code;

    private Boolean isVisible;

    private ComponentReadDto father;

    private Style border;

    private String type;

    private List<ResponsibilityReadDto> responsibilities;

    private List<VariableReadDto> variables;

}
