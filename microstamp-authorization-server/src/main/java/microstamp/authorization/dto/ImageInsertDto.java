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
public class ImageInsertDto {

    @NotBlank
    private String fileName;

    @NotBlank
    private byte[] data;

    @NotNull
    private UUID analysisId;
}
