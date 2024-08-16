package step3.dto.unsafe_control_action;

import step3.dto.value.ValueReadDto;
import step3.entity.UnsafeControlAction;

import java.util.List;

public record UnsafeControlActionContextDto(
        Long ucaId,
        String ucaName,
        List<ValueReadDto> values,
        String type,
        String rule
    ) {
    public UnsafeControlActionContextDto(UnsafeControlAction uca) {
        this(
                uca.getId(),
                uca.getName(),
                uca.getValues().stream().map(ValueReadDto::new).toList(),
                uca.getType().toString(),
                uca.getRuleTag()
        );
    }
}
