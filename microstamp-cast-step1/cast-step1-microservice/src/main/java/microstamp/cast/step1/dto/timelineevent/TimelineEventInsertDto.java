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
public class TimelineEventInsertDto {

    @NotBlank
    private String code;

    private Integer eventOrder;

    //Deixar desativado pois provavelmente é inútil
    //private Instant eventDate;

    @NotBlank
    private String eventDescription;

    private String questions;

    @NotNull
    private UUID analysisId;
}