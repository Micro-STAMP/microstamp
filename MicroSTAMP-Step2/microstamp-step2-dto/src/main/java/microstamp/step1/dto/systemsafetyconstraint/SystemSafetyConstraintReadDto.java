package microstamp.step1.dto.systemsafetyconstraint;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import microstamp.step1.dto.hazard.HazardReadDto;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemSafetyConstraintReadDto {

    @NotNull
    private UUID id;

    @NotBlank
    private String name;

    @NotBlank
    private String code;

    private List<HazardReadDto> hazards;

}