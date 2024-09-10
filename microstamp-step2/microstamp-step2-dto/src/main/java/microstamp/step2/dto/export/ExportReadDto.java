package microstamp.step2.dto.export;

import lombok.*;
import microstamp.step2.dto.component.ComponentReadDto;
import microstamp.step2.dto.connection.ConnectionReadDto;
import microstamp.step2.dto.image.ImageReadDto;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExportReadDto {

    private UUID analysisId;

    private List<ComponentReadDto> components;

    private List<ConnectionReadDto> connections;

    private List<ImageReadDto> images;

}
