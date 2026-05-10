package microstamp.cast.step1.dto.timelineevent;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;

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

    //Manter fora
    //private Instant eventDate;

    @NotBlank
    private String eventDescription;

    private String questions;
}