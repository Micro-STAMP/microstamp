package step3.controller.mit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import step3.dto.mit.safety_constraint.SafetyConstraintReadDto;
import step3.dto.mit.safety_constraint.SafetyConstraintUpdateDto;
import step3.service.mit.SafetyConstraintService;

import java.util.List;
import java.util.UUID;

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

    // Update -----------------------------------------

//    @PutMapping("/{id}") @Transactional
//    public ResponseEntity<SafetyConstraintReadDto> updateSafetyConstraint(@PathVariable UUID id, @RequestBody SafetyConstraintUpdateDto safetyConstraint) {
//        SafetyConstraintReadDto updatedSC = safetyConstraintService.updateSafetyConstraint(id, safetyConstraint);
//        return ResponseEntity.ok(updatedSC);
//    }

    // Delete -----------------------------------------


    // ------------------------------------------------
}
