package step3.service;

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
        SafetyConstraint constraint = safetyConstraintRepository.getReferenceById(id);
        return mapper.toSafetyConstraintReadDto(constraint);
    }
    public SafetyConstraintReadDto readSafetyConstraintByUCAId(UUID uca_id) {
        SafetyConstraint constraint = safetyConstraintRepository.findByUnsafeControlActionId(uca_id);
        return mapper.toSafetyConstraintReadDto(constraint);
    }
    public List<SafetyConstraintReadDto> readAllSafetyConstraints() {
        return safetyConstraintRepository.findAll()
                .stream()
                .map(mapper::toSafetyConstraintReadDto)
                .toList();
    }

//    public SafetyConstraintReadDto updateSafetyConstraint(UUID id, SafetyConstraintUpdateDto safetyConstraint) {
//        SafetyConstraint updatedSafetyConstraint = safetyConstraintRepository.getReferenceById(id);
//        updatedSafetyConstraint.setName(safetyConstraint.name());
//        return new SafetyConstraintReadDto(safetyConstraintRepository.save(updatedSafetyConstraint));
//    }
}
