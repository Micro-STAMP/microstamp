package microstamp.cast.step4.dto.systemicfactor;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import microstamp.cast.step4.entity.enums.SystemicFactorCategory;

import java.util.List;
import java.util.UUID;

public record SystemicFactorUpdateDto(

        SystemicFactorCategory category,

        @Size(max = 120)
        String customCategory,

        @NotBlank
        String description,

        List<UUID> controlActionContributionIds
) {

    @AssertTrue(message = "Either category or customCategory must be provided")
    public boolean isCategoryProvided() {
        return category != null || (customCategory != null && !customCategory.isBlank());
    }
}
