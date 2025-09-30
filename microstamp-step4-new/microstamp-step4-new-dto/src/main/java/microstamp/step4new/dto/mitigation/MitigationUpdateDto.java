package microstamp.step4new.dto.mitigation;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MitigationUpdateDto implements Serializable {

	private String mitigation;
}


