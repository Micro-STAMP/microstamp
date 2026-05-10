package microstamp.cast.step1.dto.timelineevent;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TimelineEventReadDto {

    @NotNull
    private UUID id;

    @NotBlank
    private String code;

    private Integer eventOrder;

    //Manter fora
    //private Instant eventDate;

    @NotBlank
    private String eventDescription;

    private String questions;
}