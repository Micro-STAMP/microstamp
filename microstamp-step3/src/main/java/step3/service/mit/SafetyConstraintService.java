package step3.service.mit;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import step3.dto.mit.mapper.SafetyConstraintMapper;
import step3.dto.mit.safety_constraint.SafetyConstraintReadDto;
import step3.entity.mit.SafetyConstraint;
import step3.repository.mit.SafetyConstraintRepository;

import java.util.List;
import java.util.UUID;

@Log4j2
@Service
@AllArgsConstructor
public class SafetyConstraintService {

    private final SafetyConstraintMapper mapper;

    private final SafetyConstraintRepository safetyConstraintRepository;

    public SafetyConstraintReadDto readSafetyConstraint(UUID id) {
        log.debug("Fetching for safety constraint with id {}.", id);
        SafetyConstraint constraint = safetyConstraintRepository.getReferenceById(id);

        return mapper.toSafetyConstraintReadDto(constraint);
    }

    public SafetyConstraintReadDto readSafetyConstraintByUCAId(UUID ucaId) {
        log.debug("Fetching for safety constraint with unsafe control action id {}.", ucaId);
        SafetyConstraint constraint = safetyConstraintRepository.findByUnsafeControlActionId(ucaId);

        return mapper.toSafetyConstraintReadDto(constraint);
    }

    public List<SafetyConstraintReadDto> readAllSafetyConstraints() {
        log.debug("Fetching all safety constraints.");

        return safetyConstraintRepository.findAll()
                .stream()
                .map(mapper::toSafetyConstraintReadDto)
                .toList();
    }

    // TODO: Método comentado pode ser removido?
//    public SafetyConstraintReadDto updateSafetyConstraint(UUID id, SafetyConstraintUpdateDto safetyConstraint) {
//        SafetyConstraint updatedSafetyConstraint = safetyConstraintRepository.getReferenceById(id);
//        updatedSafetyConstraint.setName(safetyConstraint.name());
//        return new SafetyConstraintReadDto(safetyConstraintRepository.save(updatedSafetyConstraint));
//    }
}
