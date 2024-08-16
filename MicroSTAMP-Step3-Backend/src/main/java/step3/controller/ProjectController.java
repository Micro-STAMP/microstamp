package step3.controller;

import step3.dto.project.*;
import step3.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {
    private final ProjectService projectService;

    // Constructors -----------------------------------

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // Create -----------------------------------------

    @PostMapping @Transactional
    public ResponseEntity<ProjectReadDto> createProject(@RequestBody @Valid ProjectCreateDto projectCreateDto, UriComponentsBuilder uriBuilder) {
        ProjectReadDto project = projectService.createProject(projectCreateDto);
        URI uri = uriBuilder.path("/project/{id}").buildAndExpand(project.id()).toUri();
        return ResponseEntity.created(uri).body(project);
    }

    // Read -------------------------------------------

    @GetMapping
    public ResponseEntity<List<ProjectReadListDto>> readAllProjects() {
        return ResponseEntity.ok(projectService.readAllProjects());
    }

    @GetMapping("/{id}")
    public  ResponseEntity<ProjectReadDto> readProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.readProjectById(id));
    }

    // Update -----------------------------------------

    // Delete -----------------------------------------

    @DeleteMapping("/{id}") @Transactional
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    // ------------------------------------------------
}
