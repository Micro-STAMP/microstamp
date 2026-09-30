package microstamp.cast.step1.dto.timelineevent;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TimelineEventUpdateDto {

    @NotBlank
    private String code;

    private Integer eventOrder;

    @NotBlank
    private String eventDescription;

    private String questions;

    private String timeLabel;
}