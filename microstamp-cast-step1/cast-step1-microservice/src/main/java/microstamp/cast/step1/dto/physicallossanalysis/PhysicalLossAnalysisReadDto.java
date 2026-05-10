package microstamp.cast.step1.dto.physicallossanalysis;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PhysicalLossAnalysisReadDto {

    @NotNull
    private UUID id;

    @NotBlank
    private String code;

    private String physicalLossDescription;

    private String affectedEquipment;

    private String physicalDesignRequirements;

    private String physicalControls;

    private String failuresAndUnsafeInteractions;

    private String missingOrInadequateControls;

    private String contextualFactors;
}