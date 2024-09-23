package step3.dto.step1;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record HazardReadDto(
        UUID id,
        String name,
        String code,
        List<LossReadDto> losses,
        HazardReadDto father
        ) {
}
