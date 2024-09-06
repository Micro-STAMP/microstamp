package microstamp.authorization.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageReadDto {

    @NotNull
    private UUID id;

    @NotBlank
    private String fileName;

    @NotBlank
    private String base64;

}
