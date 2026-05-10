package microstamp.cast.step1.dto.systemdescription;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SystemDescriptionUpdateDto {

    @NotBlank
    private String code;

    @NotBlank
    private String description;

    private String analysisBoundary;
}