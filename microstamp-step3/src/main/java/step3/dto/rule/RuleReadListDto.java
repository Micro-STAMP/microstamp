package step3.dto.rule;

import lombok.Builder;
import step3.dto.step1.HazardReadDto;
import step3.dto.step2.StateReadDto;
import step3.entity.UCAType;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Builder
public record RuleReadListDto(
        UUID id,
        String name,
        String control_action_name,
        List<StateReadDto> states,
        Set<UCAType> types,
        HazardReadDto hazard,
        String code
) {

    // Constructors -----------------------------------

//    public RuleReadListDto(Rule rule) {
//        this(
//                rule.getId(),
//                rule.getName(),
//                rule.getControlAction().getName(),
//                rule.getValues().stream().map(ValueDto::new).toList(),
//                rule.getTypes(),
//                new HazardDto(rule.getHazard()),
//                rule.getTagName()
//        );
//    }
//
//    // DTOs -------------------------------------------
//
//    private record ValueDto(Long value_id, String value_name, Long variable_id,String variable_name) {
//        public ValueDto(Value value) {
//            this(
//                    value.getId(),
//                    value.getName(),
//                    value.getVariable().getId(),
//                    value.getVariable().getName()
//            );
//        }
//    }
//    public record HazardDto(Long id, String name) {
//        public HazardDto(Hazard hazard) {
//            this(
//                    hazard.getId(),
//                    hazard.getName()
//            );
//        }
//    }

    // ------------------------------------------------
}
