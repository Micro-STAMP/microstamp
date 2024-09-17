package step3.dto.unsafe_control_action;

import step3.entity.UnsafeControlAction;

import java.util.UUID;

public record UnsafeControlActionContextDto(
        UUID ucaId,
//        String ucaName,
//        List<StateReadDto> values,
        String type,
        String rule
    ) {
    public UnsafeControlActionContextDto(UnsafeControlAction uca) {
        this(
                uca.getId(),
//                uca.getName(),
//                uca.getValues().stream().map(ValueReadDto::new).toList(),
                uca.getType().toString(),
                uca.getRuleCode()
        );
    }
}
