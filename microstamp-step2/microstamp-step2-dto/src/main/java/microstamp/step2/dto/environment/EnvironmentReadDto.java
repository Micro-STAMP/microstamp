package microstamp.step2.dto.environment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import microstamp.step2.dto.variable.VariableReadDto;
import microstamp.step2.enumeration.Style;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnvironmentReadDto {

    @NotNull
    private UUID id;

    @NotBlank
    private String name;

    @NotBlank
    private String code;

    private Boolean isVisible;

    private Style border;

    private List<VariableReadDto> variables;

}
