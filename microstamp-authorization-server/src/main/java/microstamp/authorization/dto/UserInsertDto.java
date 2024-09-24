package microstamp.authorization.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInsertDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String matchingPassword;
}
