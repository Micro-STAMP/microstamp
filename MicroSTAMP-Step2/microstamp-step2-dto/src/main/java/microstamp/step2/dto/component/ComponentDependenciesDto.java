package microstamp.step2.dto.component;

import lombok.*;
import microstamp.step2.dto.connection.ConnectionReadDto;
import microstamp.step2.dto.variable.VariableReadDto;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComponentDependenciesDto {

    private List<ComponentReadDto> components;

    private List<ConnectionReadDto> connections;

    private List<VariableReadDto> variables;
}
