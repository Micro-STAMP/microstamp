package microstamp.step1.dto.systemsafetyconstraint;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SystemSafetyConstraintUpdateDto {

    @NotBlank
    private String name;

    @NotBlank
    private String code;

    private List<UUID> hazardsId;

}
