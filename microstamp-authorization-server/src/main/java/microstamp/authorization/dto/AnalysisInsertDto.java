package microstamp.authorization.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class AnalysisInsertDto {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private UUID userId;

}
