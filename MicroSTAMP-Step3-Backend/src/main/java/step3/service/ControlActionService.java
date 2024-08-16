package step3.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import step3.dto.unsafe_control_action.UnsafeControlActionContextDto;
import step3.entity.ControlAction;
import step3.entity.Controller;
import step3.dto.control_action.*;
import step3.entity.UnsafeControlAction;
import step3.repository.ControlActionRepository;
import step3.repository.ControllerRepository;
import step3.repository.RuleRepository;
import step3.repository.UnsafeControlActionRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ControlActionService {
    private final ControlActionRepository controlActionRepository;
    private final ControllerRepository controllerRepository;
    private final UnsafeControlActionRepository ucaRepository;
    private final RuleRepository ruleRepository;

    // Create -----------------------------------------

    public ControlActionReadDto createControlAction(ControlActionCreateDto controlActionCreateDto) {
        Controller controller = controllerRepository.getReferenceById(controlActionCreateDto.controller_id());
        ControlAction controlAction = new ControlAction(controlActionCreateDto.name(), controller);
        ControlAction createdControlAction = controlActionRepository.save(controlAction);
        return new ControlActionReadDto(createdControlAction);
    }

    // Read -------------------------------------------

    public ControlActionReadDto readControlAction(Long id) {
        return new ControlActionReadDto(controlActionRepository.getReferenceById(id));
    }
    public List<ControlActionReadDto> readAllControlActions() {
        return controlActionRepository.findAll().stream().map(ControlActionReadDto::new).toList();
    }

    public List<ControlActionReadDto> readControlActionsByControllerId(Long controllerId) {
        return controlActionRepository.findByControllerId(controllerId).stream().map(ControlActionReadDto::new).toList();
    }

    public List<UnsafeControlActionContextDto> readUnsafeControlActionContext(Long id) {
        var ucaList = ucaRepository.findByControlActionId(id);
        List<UnsafeControlActionContextDto> ucaContextList = new ArrayList<>();

        for (UnsafeControlAction uca : ucaList) {
            ucaContextList.add(new UnsafeControlActionContextDto(uca));
        }

        return ucaContextList;
    }

    // Update -----------------------------------------

    public ControlActionReadDto updateControlAction(Long id, ControlActionUpdateDto controlActionDto) {
        ControlAction updatedControlAction = controlActionRepository.getReferenceById(id);
        updatedControlAction.setName(controlActionDto.name());
        return new ControlActionReadDto(controlActionRepository.save(updatedControlAction));
    }

    // Delete -----------------------------------------

    public void deleteControlAction(Long id) {
        var ruleList = ruleRepository.findByControlActionId(id);
        if (!ruleList.isEmpty()) ruleRepository.deleteAll(ruleList);

        var ucaList = ucaRepository.findByControlActionId(id);
        if (!ucaList.isEmpty()) ucaRepository.deleteAll(ucaList);

        controlActionRepository.deleteById(id);
    }



    // Methods ----------------------------------------

    // ------------------------------------------------
}
