package microstamp.step1.dto.assumption;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssumptionReadDto {

    @NotNull
    private UUID id;

    @NotBlank
    private String code;

    @NotBlank
    private String name;

}
