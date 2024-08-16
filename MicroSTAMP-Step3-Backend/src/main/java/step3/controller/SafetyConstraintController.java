package step3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import step3.dto.safety_constraint.SafetyConstraintReadDto;
import step3.dto.safety_constraint.SafetyContraintUpdateDto;
import step3.entity.SafetyConstraint;
import step3.service.SafetyConstraintService;

import java.util.List;

@RestController
@RequestMapping("/safety-constraint")
public class SafetyConstraintController {
    private final SafetyConstraintService safetyConstraintService;

    // Constructors -----------------------------------

    @Autowired
    public SafetyConstraintController(SafetyConstraintService safetyConstraintService) {
        this.safetyConstraintService = safetyConstraintService;
    }

    // Create -----------------------------------------

    // Read -------------------------------------------

    @GetMapping("/{id}")
    public ResponseEntity<SafetyConstraintReadDto> readSafetyConstraint(@PathVariable Long id) {
        return ResponseEntity.ok(safetyConstraintService.readSafetyConstraint(id));
    }
    @GetMapping("/uca/{id}")
    public ResponseEntity<SafetyConstraintReadDto> readSafetyConstraintByUCAId(@PathVariable Long id) {
        return ResponseEntity.ok(safetyConstraintService.readSafetyConstraintByUCAId(id));
    }
    @GetMapping
    public ResponseEntity<List<SafetyConstraintReadDto>> readAllSafetyConstraints() {
        return ResponseEntity.ok(safetyConstraintService.readAllSafetyConstraints());
    }

    // Update -----------------------------------------

    @PutMapping("/{id}") @Transactional
    public ResponseEntity<SafetyConstraintReadDto> updateSafetyConstraint(@PathVariable Long id, @RequestBody SafetyContraintUpdateDto safetyConstraint) {
        SafetyConstraintReadDto updatedSC = safetyConstraintService.updateSafetyConstraint(id, safetyConstraint);
        return ResponseEntity.ok(updatedSC);
    }

    // Delete -----------------------------------------


    // ------------------------------------------------
}
