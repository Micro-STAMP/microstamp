package step3.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import step3.dto.unsafe_control_action.UnsafeControlActionCreateDto;
import step3.dto.unsafe_control_action.UnsafeControlActionReadDto;
import step3.dto.unsafe_control_action.UnsafeControlActionUpdateDto;
import step3.entity.*;
import step3.infra.exceptions.OperationNotAllowedException;
import step3.repository.*;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UnsafeControlActionService {
    private final UnsafeControlActionRepository unsafeControlActionRepository;
    private final ControlActionRepository controlActionRepository;
    private final ValueRepository valueRepository;
    private final HazardRepository hazardRepository;
    private final RuleRepository ruleRepository;
    private final ProjectRepository projectRepository;

    // Create -----------------------------------------

    public UnsafeControlActionReadDto createUnsafeControlAction(UnsafeControlActionCreateDto ucaCreateDto) {
        ControlAction controlAction = controlActionRepository.getReferenceById(ucaCreateDto.control_action_id());
        List<Value> values = getUCAValues(ucaCreateDto.values_ids());
        Hazard hazard = hazardRepository.getReferenceById(ucaCreateDto.hazard_id());
        Project project = projectRepository.getReferenceById(ucaCreateDto.project_id());

        UnsafeControlAction uca = new UnsafeControlAction(
                controlAction,
                values,
                hazard,
                ucaCreateDto.type(),
                project,
                ucaCreateDto.rule_tag()
        );

        unsafeControlActionRepository.findFirstByName(uca.getName()).ifPresent(u -> {
            unsafeControlActionRepository.deleteById(u.getId());
        });

        UnsafeControlAction createdUCA = unsafeControlActionRepository.save(uca);

        return new UnsafeControlActionReadDto(createdUCA);
    }

    public List<UnsafeControlActionReadDto> createUCAsByRule(Long rule_id) {
        Rule rule = ruleRepository.getReferenceById(rule_id);
        List<UnsafeControlActionReadDto> createdUCAs = new ArrayList<>();
        // ! Gambiarra? Mas foi a única coisa que funcionou
        for (UCAType type : rule.getTypes()) {
            UnsafeControlActionCreateDto dto = new UnsafeControlActionCreateDto(
                    rule.getControlAction().getId(),
                    rule.getValues().stream().map(Value::getId).toList(),
                    rule.getHazard().getId(),
                    type,
                    rule.getControlAction().getController().getProject().getId(),
                    rule.getTagName()
            );
            createdUCAs.add(createUnsafeControlAction(dto));
        }
        return createdUCAs;
    }

    // Read -------------------------------------------

    public UnsafeControlActionReadDto readUnsafeControlAction(Long id) {
        return new UnsafeControlActionReadDto(unsafeControlActionRepository.getReferenceById(id));
    }

    public List<UnsafeControlActionReadDto> readAllUnsafeControlActions() {
        return unsafeControlActionRepository.findAll().stream().map(UnsafeControlActionReadDto::new).toList();
    }

    public List<UnsafeControlActionReadDto> readAllUCAByControlActionId(Long controlActionId) {
        return unsafeControlActionRepository
                .findByControlActionId(controlActionId)
                .stream()
                .map(UnsafeControlActionReadDto::new)
                .toList();
    }

    public List<UnsafeControlActionReadDto> readAllUCAByControllerId(Long controllerId) {
        return unsafeControlActionRepository
                .findByControlActionControllerId(controllerId)
                .stream()
                .map(UnsafeControlActionReadDto::new)
                .toList();
    }

    // Update -----------------------------------------

    public UnsafeControlActionReadDto updateUnsafeControlAction(Long id, UnsafeControlActionUpdateDto ucaDto) {
        UnsafeControlAction uca = unsafeControlActionRepository.getReferenceById(id);

        if (!uca.getRuleTag().isEmpty())
            throw new OperationNotAllowedException("Updating unsafe control actions created by rules is not allowed");

        uca.setName(ucaDto.name());
        UnsafeControlAction updatedUca = unsafeControlActionRepository.save(uca);

        return new UnsafeControlActionReadDto(unsafeControlActionRepository.save(updatedUca));
    }

    // Delete -----------------------------------------

    public void deleteUnsafeControlAction(Long id) {
        UnsafeControlAction uca = unsafeControlActionRepository.getReferenceById(id);

        if (!uca.getRuleTag().isEmpty())
            throw new OperationNotAllowedException("Removing unsafe control actions created by rules is not allowed");

        unsafeControlActionRepository.deleteById(id);
    }

    // Methods ----------------------------------------

    public List<Value> getUCAValues(List<Long> valuesIds) {
        List<Value> values = new ArrayList<>();
        for (Long value_id : valuesIds) {
            Value value = valueRepository.getReferenceById(value_id);
            values.add(value);
        }
        return values;
    }

    // ------------------------------------------------
}
