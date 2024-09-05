package microstamp.step2.dto.component;

import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import microstamp.step2.enumeration.ComponentType;
import microstamp.step2.enumeration.Style;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComponentUpdateDto {

    @NotBlank
    private String name;

    @NotBlank
    private String code;

    private Boolean isVisible;

    private UUID fatherId;

    private Style border;

    private ComponentType type;

    @AssertFalse(message = "Cannot create a Component named 'Environment'. Use the default Environment Component for operations involving the Environment.")
    private boolean isNameEqualsEnvironment() {
        return ("Environment").equals(name);
    }
}
