package microstamp.cast.step1.dto.systemdescription;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SystemDescriptionReadDto {

    @NotNull
    private UUID id;

    @NotBlank
    private String code;

    @NotBlank
    private String description;

    private String analysisBoundary;
}