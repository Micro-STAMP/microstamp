package step3.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import step3.dto.hazard.*;
import step3.entity.Hazard;
import step3.service.HazardService;
import java.util.List;

@RestController
@RequestMapping("/hazard")
public class HazardController {
    private final HazardService hazardService;

    // Constructors -----------------------------------

    @Autowired
    public HazardController(HazardService hazardService) {
        this.hazardService = hazardService;
    }

    // Create -----------------------------------------

    @PostMapping @Transactional
    public ResponseEntity<HazardReadDto> createHazard(@RequestBody @Valid HazardCreateDto hazardCreateDto, UriComponentsBuilder uriBuilder) {
        HazardReadDto hazard = hazardService.createHazard(hazardCreateDto);
        URI uri = uriBuilder.path("/hazard/{id}").buildAndExpand(hazard.id()).toUri();
        return ResponseEntity.created(uri).body(hazard);
    }

    // Read -------------------------------------------

    @GetMapping("/{id}")
    public ResponseEntity<HazardReadDto> readHazard(@PathVariable Long id) {
        return ResponseEntity.ok(hazardService.readHazard(id));
    }
    @GetMapping
    public ResponseEntity<List<HazardReadDto>> readAllHazards() {
        return ResponseEntity.ok(hazardService.readAllHazards());
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<HazardReadDto>> readHazardsByProjectId(@PathVariable Long projectId) {
        return ResponseEntity.ok(hazardService.readHazardsByProjectId(projectId));
    }

    // Update -----------------------------------------

    @PutMapping("/{id}") @Transactional
    public ResponseEntity<HazardReadDto> updateHazard(@PathVariable Long id, @RequestBody HazardUpdateDto hazardUpdateDto) {
        HazardReadDto uptadedHazard = hazardService.updateHazard(id, hazardUpdateDto);
        return ResponseEntity.ok(uptadedHazard);
    }

    // Delete -----------------------------------------

    @DeleteMapping("/{id}") @Transactional
    public ResponseEntity<Void> deleteHazard(@PathVariable Long id) {
        hazardService.deleteHazard(id);
        return ResponseEntity.noContent().build();
    }

    // ------------------------------------------------
}
