package microstamp.authorization.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserReadDto {

    @NotNull
    private UUID id;

    @NotBlank
    private String username;

    private List<AnalysisReadDto> analyses;

}
