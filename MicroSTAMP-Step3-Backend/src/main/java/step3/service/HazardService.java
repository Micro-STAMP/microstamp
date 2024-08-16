package step3.service;

import org.springframework.stereotype.Service;
import step3.dto.hazard.HazardCreateDto;
import step3.dto.hazard.HazardReadDto;
import step3.dto.hazard.HazardUpdateDto;
import step3.entity.Hazard;
import step3.entity.Project;
import step3.repository.HazardRepository;
import step3.repository.ProjectRepository;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class HazardService {
    private final HazardRepository hazardRepository;
    private final ProjectRepository projectRepository;
    private int nextTag;

    public HazardService(HazardRepository hazardRepository, ProjectRepository projectRepository) {
        this.hazardRepository = hazardRepository;
        this.projectRepository = projectRepository;

        int hazardListSize = hazardRepository.findAll().size();
        this.nextTag = hazardListSize == 0 ? 1 : hazardListSize + 1;
    }


    // Create -----------------------------------------

    public HazardReadDto createHazard(HazardCreateDto hazardCreateDto) {
        Project project = projectRepository.getReferenceById(hazardCreateDto.project_id());
        Hazard hazard = new Hazard(hazardCreateDto.name(), project);
        hazard.setTag(nextTag++);
        Hazard createdHazard = hazardRepository.save(hazard);
        return new HazardReadDto(createdHazard);
    }

    // Read -------------------------------------------

    public HazardReadDto readHazard(Long id) {
        return new HazardReadDto(hazardRepository.getReferenceById(id));
    }

    public List<HazardReadDto> readAllHazards() {
        return hazardRepository.findAll().stream().map(HazardReadDto::new).toList();
    }

    public List<HazardReadDto> readHazardsByProjectId(Long projectId) {
        return hazardRepository.findByProjectId(projectId).stream().map(HazardReadDto::new).toList();
    }

    // Update -----------------------------------------

    public HazardReadDto updateHazard(Long id, HazardUpdateDto hazardDTO) {
        Hazard updatedHazard = hazardRepository.getReferenceById(id);
        updatedHazard.setName(hazardDTO.name());
        return new HazardReadDto(hazardRepository.save(updatedHazard));
    }

    // Delete -----------------------------------------

    public void deleteHazard(Long id) {
        hazardRepository.deleteById(id);
        updateTags();
        this.nextTag--;
    }

    // Methods ----------------------------------------

    private void updateTags() {
        AtomicInteger newTag = new AtomicInteger(1);
        var hazards = hazardRepository.findAll();
        hazards.forEach(hazard -> hazard.setTag(newTag.getAndIncrement()));
    }

    // ------------------------------------------------
}
