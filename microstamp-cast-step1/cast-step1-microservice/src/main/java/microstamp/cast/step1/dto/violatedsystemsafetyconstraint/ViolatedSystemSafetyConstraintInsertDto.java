package microstamp.cast.step1.dto.violatedsystemsafetyconstraint;

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
public class ViolatedSystemSafetyConstraintInsertDto {

    @NotBlank
    private String code;

    @NotBlank
    private String name;

    private String description;

    private List<UUID> hazardIds;

    @NotNull
    private UUID analysisId;
}