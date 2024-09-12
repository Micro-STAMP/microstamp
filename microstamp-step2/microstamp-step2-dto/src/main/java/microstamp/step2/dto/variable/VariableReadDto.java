package microstamp.step2.dto.variable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VariableReadDto {

    @NotNull
    private UUID id;

    @NotBlank
    private String name;

    @NotBlank
    private String code;

}