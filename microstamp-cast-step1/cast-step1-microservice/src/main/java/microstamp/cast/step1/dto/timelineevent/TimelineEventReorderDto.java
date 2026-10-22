package microstamp.cast.step1.dto.timelineevent;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public record TimelineEventReorderDto(

        @NotEmpty
        List<UUID> orderedIds
) {}
