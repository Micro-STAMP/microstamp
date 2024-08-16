package microstamp.step2.dto.controlstructure;

import lombok.*;
import microstamp.step2.dto.component.ComponentReadDto;
import microstamp.step2.dto.connection.ConnectionReadDto;
import microstamp.step2.dto.image.ImageReadDto;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ControlStructureReadDto {

    private List<ComponentReadDto> components;

    private List<ConnectionReadDto> connections;

    private List<ImageReadDto> images;
}
