package step3.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import step3.dto.safety_constraint.SafetyConstraintReadDto;
import step3.dto.safety_constraint.SafetyContraintUpdateDto;
import step3.entity.SafetyConstraint;
import step3.repository.SafetyConstraintRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class SafetyConstraintService {
    private final SafetyConstraintRepository safetyConstraintRepository;

    // Create -----------------------------------------

    // Read -------------------------------------------

    public SafetyConstraintReadDto readSafetyConstraint(Long id) {
        return new SafetyConstraintReadDto(safetyConstraintRepository.getReferenceById(id));
    }
    public SafetyConstraintReadDto readSafetyConstraintByUCAId(Long uca_id) {
        return new SafetyConstraintReadDto(safetyConstraintRepository.findByUnsafeControlActionId(uca_id));
    }
    public List<SafetyConstraintReadDto> readAllSafetyConstraints() {
        return safetyConstraintRepository.findAll().stream().map(SafetyConstraintReadDto::new).toList();
    }

    // Update -----------------------------------------

    public SafetyConstraintReadDto updateSafetyConstraint(Long id, SafetyContraintUpdateDto safetyConstraint) {
        SafetyConstraint updatedSafetyConstraint = safetyConstraintRepository.getReferenceById(id);
        updatedSafetyConstraint.setName(safetyConstraint.name());
        return new SafetyConstraintReadDto(safetyConstraintRepository.save(updatedSafetyConstraint));
    }

    // Delete -----------------------------------------


    // Methods ----------------------------------------

    // ------------------------------------------------
}
