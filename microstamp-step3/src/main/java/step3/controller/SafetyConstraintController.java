package step3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import step3.dto.safety_constraint.SafetyConstraintReadDto;
import step3.service.SafetyConstraintService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/safety-constraint")
public class SafetyConstraintController {
    private final SafetyConstraintService safetyConstraintService;

    @Autowired
    public SafetyConstraintController(SafetyConstraintService safetyConstraintService) {
        this.safetyConstraintService = safetyConstraintService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SafetyConstraintReadDto> readSafetyConstraint(@PathVariable UUID id) {
        return ResponseEntity.ok(safetyConstraintService.readSafetyConstraint(id));
    }
    @GetMapping("/uca/{id}")
    public ResponseEntity<SafetyConstraintReadDto> readSafetyConstraintByUCAId(@PathVariable UUID id) {
        return ResponseEntity.ok(safetyConstraintService.readSafetyConstraintByUCAId(id));
    }
    @GetMapping
    public ResponseEntity<List<SafetyConstraintReadDto>> readAllSafetyConstraints() {
        return ResponseEntity.ok(safetyConstraintService.readAllSafetyConstraints());
    }
}
