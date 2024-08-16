package step3.dto.mit.step1;

import java.util.List;
import java.util.UUID;

public record HazardReadDto(
        UUID id,
        String name,
        String code,
        List<LossReadDto> losses,
        HazardReadDto father
        ) {
}
