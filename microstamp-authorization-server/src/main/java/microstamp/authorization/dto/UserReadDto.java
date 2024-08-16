package microstamp.authorization.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UserReadDto {

    @NotNull
    private UUID id;

    @NotBlank
    private String username;

    private List<AnalysisReadDto> analyses;

}
