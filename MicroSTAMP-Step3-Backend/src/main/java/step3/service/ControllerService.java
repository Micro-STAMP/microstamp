package step3.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import step3.dto.controller.*;
import step3.entity.Controller;
import step3.entity.Project;
import step3.repository.ControllerRepository;
import step3.repository.ProjectRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ControllerService {
    private final ControllerRepository controllerRepository;
    private final ProjectRepository projectRepository;

    // Create -----------------------------------------

    public ControllerReadDto createController(ControllerCreateDto controllerCreateDto) {
        Project project = projectRepository.getReferenceById(controllerCreateDto.project_id());
        Controller controller = new Controller(controllerCreateDto.name(), project);
        Controller createdController = controllerRepository.save(controller);
        return new ControllerReadDto(createdController);
    }

    // Read -------------------------------------------

    public List<ControllerReadListDto> readControllersByProjectId(Long projectId) {
        List<Controller> controllers = controllerRepository.findByProjectId(projectId);
        return controllers.stream()
                .map(ControllerReadListDto::new)
                .toList();
    }


    public ControllerReadDto readControllerById(Long id) {
        Controller controller = controllerRepository.getReferenceById(id);
        return new ControllerReadDto(controller);
    }

    public List<ControllerReadListDto> readAllControllers() {
        return controllerRepository.findAll()
                .stream()
                .map(ControllerReadListDto::new)
                .toList();
    }

    // Update -----------------------------------------

    public ControllerReadDto updateController(Long id, ControllerUpdateDto controllerDto) {
        Controller updatedController = controllerRepository.getReferenceById(id);
        updatedController.setName(controllerDto.name());
        return new ControllerReadDto(controllerRepository.save(updatedController));
    }

    public ControllerReadDto deleteContextTable(Long controllerId) {
        var controller = controllerRepository.getReferenceById(controllerId);
        controller.setContextTable(null);
        var controllerWithoutContextTable = controllerRepository.save(controller);
        return new ControllerReadDto(controllerWithoutContextTable);
    }

    // Delete -----------------------------------------

    public void deleteController(Long id) {
        controllerRepository.deleteById(id);
    }

    // Methods ----------------------------------------

    // ------------------------------------------------
}

