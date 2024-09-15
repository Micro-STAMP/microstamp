package step3.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import step3.dto.mapper.SafetyConstraintMapper;
import step3.dto.safety_constraint.SafetyConstraintReadDto;
import step3.entity.SafetyConstraint;
import step3.repository.SafetyConstraintRepository;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SafetyConstraintService {
    private final SafetyConstraintRepository safetyConstraintRepository;
    private final SafetyConstraintMapper mapper;

    public SafetyConstraintReadDto readSafetyConstraint(UUID id) {
        SafetyConstraint constraint = safetyConstraintRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Safety Constraint not found with id: " + id));

        return mapper.toSafetyConstraintReadDto(constraint);
    }
    public SafetyConstraintReadDto readSafetyConstraintByUCAId(UUID uca_id) {
        SafetyConstraint constraint = safetyConstraintRepository
                .findByUnsafeControlActionId(uca_id)
                .orElseThrow(() -> new EntityNotFoundException("Safety constraint not found with unsafe control action id: " + uca_id));

        return mapper.toSafetyConstraintReadDto(constraint);
    }
    public List<SafetyConstraintReadDto> readAllSafetyConstraints() {
        return safetyConstraintRepository.findAll()
                .stream()
                .map(mapper::toSafetyConstraintReadDto)
                .toList();
    }
}
