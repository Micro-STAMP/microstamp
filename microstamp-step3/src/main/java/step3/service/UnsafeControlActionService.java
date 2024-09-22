package step3.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import step3.dto.mapper.UnsafeControlActionMapper;
import step3.dto.step2.ControlActionReadDto;
import step3.dto.step2.StateReadDto;
import step3.dto.unsafe_control_action.UnsafeControlActionCreateDto;
import step3.dto.unsafe_control_action.UnsafeControlActionReadDto;
import step3.entity.Rule;
import step3.entity.SafetyConstraint;
import step3.entity.UCAType;
import step3.entity.UnsafeControlAction;
import step3.entity.association.RuleState;
import step3.entity.association.UnsafeControlActionState;
import step3.infra.exceptions.OperationNotAllowedException;
import step3.proxy.AuthServerProxy;
import step3.proxy.Step1Proxy;
import step3.proxy.Step2Proxy;
import step3.repository.RuleRepository;
import step3.repository.StateAssociationRepository;
import step3.repository.UnsafeControlActionRepository;

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
    private final AuthServerProxy authServerProxy;
    private final UnsafeControlActionMapper mapper;

    public UnsafeControlActionReadDto createUnsafeControlAction(UnsafeControlActionCreateDto ucaCreateDto) {
        authServerProxy.getAnalysisById(ucaCreateDto.analysis_id());
        step1Proxy.getHazardById(ucaCreateDto.hazard_id());
        ControlActionReadDto controlAction = step2Proxy.getControlActionById(ucaCreateDto.control_action_id());

        UnsafeControlAction uca = UnsafeControlAction.builder()
                .controlActionId(controlAction.id())
                .hazardId(ucaCreateDto.hazard_id())
                .type(ucaCreateDto.type())
                .analysisId(ucaCreateDto.analysis_id())
                .ruleCode(ucaCreateDto.rule_code() == null ? "" : ucaCreateDto.rule_code())
                .build();

        SafetyConstraint constraint = SafetyConstraint.builder()
                .unsafeControlAction(uca)
                .build();

        uca.setConstraint(constraint);

        UnsafeControlAction createdUCA = unsafeControlActionRepository.save(uca);

        List<UnsafeControlActionState> stateAssociations = new ArrayList<>();
        for (UUID stateId : ucaCreateDto.states_ids()) {
            step2Proxy.getStateById(stateId);

            UnsafeControlActionState stateAssociation = UnsafeControlActionState.builder()
                    .unsafeControlAction(createdUCA)
                    .stateId(stateId)
                    .build();
            stateAssociations.add(stateAssociation);
        }
        createdUCA.setStateAssociations(stateAssociations);
        createdUCA = unsafeControlActionRepository.save(uca);

        return mapper.toUcaReadDto(createdUCA);
    }

    public List<UnsafeControlActionReadDto> createUCAsByRule(UUID ruleId) {
        Rule rule = ruleRepository.findById(ruleId)
                .orElseThrow(() -> new EntityNotFoundException("Rule not found with id " + ruleId));

        if (rule.isAlreadyApplied()) {
            throw new OperationNotAllowedException("Rule with id " + ruleId + " has already been applied");
        }
        rule.setAlreadyApplied(true);
        ruleRepository.save(rule);

        List<UnsafeControlActionReadDto> createdUCAs = new ArrayList<>();

        for (UCAType type : rule.getTypes()) {
            UnsafeControlActionCreateDto dto = UnsafeControlActionCreateDto.builder()
                    .control_action_id(rule.getControlActionId())
                    .hazard_id(rule.getHazardId())
                    .analysis_id(rule.getAnalysisId())
                    .rule_code(rule.getCode())
                    .type(type)
                    .states_ids(rule.getStateAssociations()
                            .stream()
                            .map(RuleState::getStateId)
                            .toList())
                    .build();

            createdUCAs.add(createUnsafeControlAction(dto));
        }
        return createdUCAs;
    }

    public UnsafeControlActionReadDto readUnsafeControlAction(UUID id) {
        UnsafeControlAction uca = unsafeControlActionRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Unsafe control action not found with id " + id));

        return mapper.toUcaReadDto(uca);
    }

    public List<UnsafeControlActionReadDto> readAllUnsafeControlActions() {
        List<UnsafeControlAction> unsafeControlActions = unsafeControlActionRepository.findAll();
        return mapper.toUcaReadDtoList(unsafeControlActions);
    }

    public List<UnsafeControlActionReadDto> readAllUCAByControlActionId(UUID controlActionId) {
        step2Proxy.getControlActionById(controlActionId);
        List<UnsafeControlAction> unsafeControlActions = unsafeControlActionRepository.findByControlActionId(controlActionId);
        return mapper.toUcaReadDtoList(unsafeControlActions);
    }

    public List<UnsafeControlActionReadDto> readAllUCAByAnalysisId(UUID analysisId) {
        authServerProxy.getAnalysisById(analysisId);
        List<UnsafeControlAction> unsafeControlActions = unsafeControlActionRepository.findByAnalysisId(analysisId);
        return mapper.toUcaReadDtoList(unsafeControlActions);
    }

    public void deleteUnsafeControlAction(UUID id) {
        UnsafeControlAction uca = unsafeControlActionRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Unsafe control action not found with id " + id));

        if (StringUtils.isNotBlank(uca.getRuleCode()))
            throw new OperationNotAllowedException("Removing unsafe control actions created by rules is not allowed");

        stateAssociationRepository.deleteAllByUnsafeControlActionId(id);
        unsafeControlActionRepository.deleteById(id);
    }

    public List<StateReadDto> getUCAStates(List<UUID> statesIds) {
        List<StateReadDto> states = new ArrayList<>();
        for (UUID stateId : statesIds) {
            StateReadDto state = step2Proxy.getStateById(stateId);
            states.add(state);
        }
        return states;
    }
}
