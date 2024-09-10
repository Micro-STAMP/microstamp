package microstamp.authorization.dto.step2;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Step2ExportReadDto {

    private List<ComponentReadDto> components;

    private List<ConnectionReadDto> connections;

    private List<ImageReadDto> images;

}
