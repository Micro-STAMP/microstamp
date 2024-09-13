package microstamp.authorization.dto.step3;

import microstamp.authorization.dto.step1.LossReadDto;

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
