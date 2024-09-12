package step3.dto.rule;

import lombok.Builder;
import step3.dto.step1.HazardReadDto;
import step3.dto.step2.ControlActionReadDto;
import step3.dto.step2.StateReadDto;
import step3.entity.Rule;
import step3.entity.UCAType;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Builder
public record RuleReadDto(
        UUID id,
        String name,
        ControlActionReadDto control_action,
        List<StateReadDto> states,
        Set<UCAType> types,
        HazardReadDto hazard,
        String code
) {

    // Constructors -----------------------------------

    public RuleReadDto(Rule rule, ControlActionReadDto controlAction, List<StateReadDto> states, HazardReadDto hazard) {
        this(
                rule.getId(),
                rule.getName(),
                controlAction,
                states,
                rule.getTypes(),
                hazard,
                rule.getCodeName()
        );
    }

    // DTOs -------------------------------------------

//    private record ControlActionDto(UUID id, String name) {
//        public ControlActionDto(ControlActionReadDto controlAction) {
//            this(
//                    controlAction.id(),
//                    controlAction.name()
//            );
//        }
//    }
//
//    private record ValueDto(UUID value_id, /*String variable_name,*/ String state_name) {
//        public ValueDto(StateReadDto state) {
//            this(
//                    state.id(),
//                    /*state.getVariable().getName(),*/
//                    state.name()
//            );
//        }
//    }
//
//    public record HazardDto(UUID id, String name) {
//        public HazardDto(HazardReadDto hazard) {
//            this(
//                    hazard.id(),
//                    hazard.name()
//            );
//        }
//    }

    // ------------------------------------------------
}
