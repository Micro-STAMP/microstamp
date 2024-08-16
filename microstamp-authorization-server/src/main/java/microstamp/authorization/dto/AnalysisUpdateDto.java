package microstamp.authorization.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AnalysisUpdateDto {

    @NotBlank
    private String name;

    private String description;

}
