package microstamp.auth.dto.analysis;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
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
