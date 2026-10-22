package microstamp.cast.step1.dto.timelineevent;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.List;
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

    @NotBlank
    private String eventDescription;

    private List<String> questions;

    private String timeLabel;

    private Instant exactDateTime;

    @NotNull
    private UUID analysisId;
}