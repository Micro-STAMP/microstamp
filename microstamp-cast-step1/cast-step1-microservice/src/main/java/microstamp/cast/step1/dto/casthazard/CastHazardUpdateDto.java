package microstamp.cast.step1.dto.casthazard;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CastHazardUpdateDto {

    @NotBlank
    private String code;

    @NotBlank
    private String name;

    private String description;

    private List<UUID> accidentLossEventIds;
}