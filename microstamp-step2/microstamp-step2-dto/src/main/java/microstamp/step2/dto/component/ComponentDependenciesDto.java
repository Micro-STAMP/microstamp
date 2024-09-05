package microstamp.step2.dto.component;

import lombok.*;
import microstamp.step2.dto.connection.ConnectionReadDto;
import microstamp.step2.dto.variable.VariableReadDto;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComponentDependenciesDto {

    private List<ComponentReadDto> components = new ArrayList<>();

    private List<ConnectionReadDto> connections = new ArrayList<>();

    private List<VariableReadDto> variables = new ArrayList<>();
}
