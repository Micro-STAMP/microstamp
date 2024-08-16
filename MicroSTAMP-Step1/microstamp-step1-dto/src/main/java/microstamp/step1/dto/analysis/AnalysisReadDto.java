package microstamp.step1.dto.analysis;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
public class AnalysisReadDto {

    @NotNull
    private UUID id;

    @NotBlank
    private String name;

    private String description;

    private Instant creationDate;

    @NotNull
    private UUID userId;

}
