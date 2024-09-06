package microstamp.authorization.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisUpdateDto {

    @NotBlank
    private String name;

    private String description;

}
