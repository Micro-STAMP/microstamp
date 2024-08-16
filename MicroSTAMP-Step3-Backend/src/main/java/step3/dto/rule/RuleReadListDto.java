package step3.dto.rule;

import step3.entity.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public record RuleReadListDto(
        Long id,
        String name,
        String control_action_name,
        List<ValueDto> values,
        Set<UCAType> types,
        HazardDto hazard,
        String tag
) {

    // Constructors -----------------------------------

    public RuleReadListDto(Rule rule) {
        this(
                rule.getId(),
                rule.getName(),
                rule.getControlAction().getName(),
                rule.getValues().stream().map(ValueDto::new).toList(),
                rule.getTypes(),
                new HazardDto(rule.getHazard()),
                rule.getTagName()
        );
    }

    // DTOs -------------------------------------------

    private record ValueDto(Long value_id, String value_name, Long variable_id,String variable_name) {
        public ValueDto(Value value) {
            this(
                    value.getId(),
                    value.getName(),
                    value.getVariable().getId(),
                    value.getVariable().getName()
            );
        }
    }
    public record HazardDto(Long id, String name) {
        public HazardDto(Hazard hazard) {
            this(
                    hazard.getId(),
                    hazard.getName()
            );
        }
    }

    // ------------------------------------------------
}
