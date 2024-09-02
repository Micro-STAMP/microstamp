package step3.dto.mit.unsafe_control_action;

import step3.dto.mit.step2.StateReadDto;
import step3.entity.mit.UnsafeControlAction;

import java.util.List;
import java.util.UUID;

public record UnsafeControlActionContextDto(
        UUID ucaId,
        String ucaName,
//        List<StateReadDto> values,
        String type,
        String rule
    ) {
    public UnsafeControlActionContextDto(UnsafeControlAction uca) {
        this(
                uca.getId(),
                uca.getName(),
//                uca.getValues().stream().map(ValueReadDto::new).toList(),
                uca.getType().toString(),
                uca.getRuleCode()
        );
    }
}
