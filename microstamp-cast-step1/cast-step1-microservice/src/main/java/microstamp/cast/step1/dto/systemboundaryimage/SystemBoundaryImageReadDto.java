package microstamp.cast.step1.dto.systemboundaryimage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemBoundaryImageReadDto {

    @NotNull
    private UUID id;

    @NotBlank
    private String name;

    @NotBlank
    private String base64;

}
