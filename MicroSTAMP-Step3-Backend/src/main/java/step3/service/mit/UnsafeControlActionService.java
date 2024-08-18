package step3.service.mit;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import step3.dto.mit.step1.HazardReadDto;
import step3.dto.mit.step2.ControlActionReadDto;
import step3.dto.mit.step2.StateReadDto;
import step3.dto.mit.unsafe_control_action.UnsafeControlActionCreateDto;
import step3.dto.mit.unsafe_control_action.UnsafeControlActionReadDto;
import step3.entity.mit.Rule;
import step3.entity.mit.UCAType;
import step3.entity.mit.UnsafeControlAction;
import step3.entity.mit.association.RuleState;
import step3.entity.mit.association.UnsafeControlActionState;
import step3.infra.exceptions.OperationNotAllowedException;
import step3.proxy.Step1Proxy;
import step3.proxy.Step2Proxy;
import step3.repository.mit.RuleRepository;
import step3.repository.mit.StateAssociationRepository;
import step3.repository.mit.UnsafeControlActionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UnsafeControlActionService {
    private final UnsafeControlActionRepository unsafeControlActionRepository;
    private final StateAssociationRepository stateAssociationRepository;
    private final RuleRepository ruleRepository;
    private final Step1Proxy step1Proxy;
    private final Step2Proxy step2Proxy;

    // Create -----------------------------------------

    public UnsafeControlActionReadDto createUnsafeControlAction(UnsafeControlActionCreateDto ucaCreateDto) {
        ControlActionReadDto controlAction = step2Proxy.getControlActionById(ucaCreateDto.control_action_id());
        List<StateReadDto> states = getUCAStates(ucaCreateDto.states_ids());
        HazardReadDto hazard = step1Proxy.getHazardById(ucaCreateDto.hazard_id());
//        Project project = projectRepository.getReferenceById(ucaCreateDto.project_id());

//        UnsafeControlAction uca = new UnsafeControlAction(
//                controlAction,
//                values,
//                hazard,
//                ucaCreateDto.type(),
//                project,
//                ucaCreateDto.rule_tag()
//        );

        UnsafeControlAction uca = UnsafeControlAction.builder()
                .controlActionId(controlAction.id())
                .hazardId(ucaCreateDto.hazard_id())
                .type(ucaCreateDto.type())
                .analysisId(ucaCreateDto.analysis_id())
                .ruleCode(ucaCreateDto.rule_code())
                .build();

//        unsafeControlActionRepository.findFirstByName(uca.getName()).ifPresent(u -> {
//            unsafeControlActionRepository.deleteById(u.getId());
//        });

        UnsafeControlAction createdUCA = unsafeControlActionRepository.save(uca);

        //verificar funcionamento disso mais tarde
        List<UnsafeControlActionState> stateAssociations = new ArrayList<>();
        for (UUID stateId : ucaCreateDto.states_ids()) {
            UnsafeControlActionState stateAssociation = UnsafeControlActionState.builder()
                    .unsafeControlActionId(createdUCA.getId())
                    .stateId(stateId)
                    .build();
            stateAssociations.add(stateAssociation);
        }
        createdUCA.setStateAssociations(stateAssociations);
        createdUCA = unsafeControlActionRepository.save(uca);

        return new UnsafeControlActionReadDto(createdUCA.getId(), createdUCA.getName(), hazard.code(), ucaCreateDto.rule_code());
    }

    public List<UnsafeControlActionReadDto> createUCAsByRule(UUID ruleId) {
        Rule rule = ruleRepository.getReferenceById(ruleId);
        List<UnsafeControlActionReadDto> createdUCAs = new ArrayList<>();
        // ! Gambiarra? Mas foi a única coisa que funcionou
        for (UCAType type : rule.getTypes()) {
            UnsafeControlActionCreateDto dto = UnsafeControlActionCreateDto.builder()
                    .control_action_id(rule.getControlActionId())
                    .hazard_id(rule.getHazardId())
                    .analysis_id(rule.getAnalysisId())
                    .rule_code(rule.getCodeName())
                    .states_ids(rule.getStateAssociations()
                            .stream()
                            .map(RuleState::getStateId)
                            .toList())
                    .build();

            createdUCAs.add(createUnsafeControlAction(dto));
        }
        return createdUCAs;
    }

//    // Read -------------------------------------------
//
//    public UnsafeControlActionReadDto readUnsafeControlAction(Long id) {
//        return new UnsafeControlActionReadDto(unsafeControlActionRepository.getReferenceById(id));
//    }
//
//    public List<UnsafeControlActionReadDto> readAllUnsafeControlActions() {
//        return unsafeControlActionRepository.findAll().stream().map(UnsafeControlActionReadDto::new).toList();
//    }
//
//    public List<UnsafeControlActionReadDto> readAllUCAByControlActionId(Long controlActionId) {
//        return unsafeControlActionRepository
//                .findByControlActionId(controlActionId)
//                .stream()
//                .map(UnsafeControlActionReadDto::new)
//                .toList();
//    }
//
//    public List<UnsafeControlActionReadDto> readAllUCAByControllerId(Long controllerId) {
//        return unsafeControlActionRepository
//                .findByControlActionControllerId(controllerId)
//                .stream()
//                .map(UnsafeControlActionReadDto::new)
//                .toList();
//    }
//
//    // Update -----------------------------------------
//
//    public UnsafeControlActionReadDto updateUnsafeControlAction(Long id, UnsafeControlActionUpdateDto ucaDto) {
//        UnsafeControlAction uca = unsafeControlActionRepository.getReferenceById(id);
//
//        if (!uca.getRuleTag().isEmpty())
//            throw new OperationNotAllowedException("Updating unsafe control actions created by rules is not allowed");
//
//        uca.setName(ucaDto.name());
//        UnsafeControlAction updatedUca = unsafeControlActionRepository.save(uca);
//
//        return new UnsafeControlActionReadDto(unsafeControlActionRepository.save(updatedUca));
//    }
//
//    // Delete -----------------------------------------

    public void deleteUnsafeControlAction(UUID id) {
        UnsafeControlAction uca = unsafeControlActionRepository.getReferenceById(id);

        if (!uca.getRuleCode().isEmpty())
            throw new OperationNotAllowedException("Removing unsafe control actions created by rules is not allowed");

        stateAssociationRepository.deleteAllByUnsafeControlActionId(id);
        unsafeControlActionRepository.deleteById(id);
    }

    // Methods ----------------------------------------

//    public List<Value> getUCAValues(List<Long> valuesIds) {
//        List<Value> values = new ArrayList<>();
//        for (Long value_id : valuesIds) {
//            Value value = valueRepository.getReferenceById(value_id);
//            values.add(value);
//        }
//        return values;
//    }

    public List<StateReadDto> getUCAStates(List<UUID> statesIds) {
        List<StateReadDto> states = new ArrayList<>();
        for (UUID stateId : statesIds) {
            StateReadDto state = step2Proxy.getStateById(stateId);
            states.add(state);
        }
        return states;
    }

    // ------------------------------------------------
}