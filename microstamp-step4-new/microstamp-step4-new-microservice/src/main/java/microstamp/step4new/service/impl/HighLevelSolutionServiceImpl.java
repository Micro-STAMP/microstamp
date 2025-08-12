package microstamp.step4new.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.step4new.dto.highlevelsolution.HighLevelSolutionInsertDto;
import microstamp.step4new.dto.highlevelsolution.HighLevelSolutionReadDto;
import microstamp.step4new.dto.highlevelsolution.HighLevelSolutionUpdateDto;
import microstamp.step4new.entity.FormalScenarioClass;
import microstamp.step4new.entity.HighLevelSolution;
import microstamp.step4new.mapper.HighLevelSolutionMapper;
import microstamp.step4new.exception.Step4NewIllegalArgumentException;
import microstamp.step4new.exception.Step4NewNotFoundException;
import microstamp.step4new.repository.FormalScenarioClassRepository;
import microstamp.step4new.repository.HighLevelSolutionRepository;
import microstamp.step4new.service.HighLevelSolutionService;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.List;

@Log4j2
@Component
@AllArgsConstructor
public class HighLevelSolutionServiceImpl implements HighLevelSolutionService {

    private final HighLevelSolutionRepository repository;
    private final FormalScenarioClassRepository formalScenarioClassRepository;

    @Override
    public HighLevelSolutionReadDto findById(UUID id) {
        log.info("Finding HighLevelSolution by id: {}", id);
        HighLevelSolution entity = repository.findById(id)
                .orElseThrow(() -> new Step4NewNotFoundException("HighLevelSolution", id.toString()));
        return HighLevelSolutionMapper.toDto(entity);
    }

    @Override
    public HighLevelSolutionReadDto findByFormalScenarioClassId(UUID formalScenarioClassId) {
        log.info("Finding HighLevelSolution by FormalScenarioClass id: {}", formalScenarioClassId);
        HighLevelSolution entity = repository.findByFormalScenarioClassId(formalScenarioClassId)
                .orElseThrow(() -> new Step4NewNotFoundException("HighLevelSolution", formalScenarioClassId.toString()));
        return HighLevelSolutionMapper.toDto(entity);
    }

    @Override
    public HighLevelSolutionReadDto create(HighLevelSolutionInsertDto dto) {
        log.debug("Verifying if the HighLevelSolution insert is valid");
        FormalScenarioClass formalScenarioClass = formalScenarioClassRepository.findById(dto.getFormalScenarioClassId())
                .orElseThrow(() -> new Step4NewNotFoundException("FormalScenarioClass", dto.getFormalScenarioClassId().toString()));

        if (repository.findByFormalScenarioClassId(formalScenarioClass.getId()).isPresent())
            throw new Step4NewIllegalArgumentException("HighLevelSolution already exists for FormalScenarioClass: " + formalScenarioClass.getId());

        log.info("Inserting HighLevelSolution for class {} on the database", formalScenarioClass.getId());
        HighLevelSolution saved = repository.save(HighLevelSolutionMapper.toEntity(dto, formalScenarioClass));
        return HighLevelSolutionMapper.toDto(saved);
    }

    @Override
    public void update(UUID id, HighLevelSolutionUpdateDto dto) {
        log.debug("Verifying if the HighLevelSolution update is valid");
        HighLevelSolution entity = repository.findById(id)
                .orElseThrow(() -> new Step4NewNotFoundException("HighLevelSolution", id.toString()));
        log.info("Updating the HighLevelSolution with id {}", id);
        HighLevelSolutionMapper.applyUpdate(entity, dto);
        repository.save(entity);
    }

    @Override
    public void delete(UUID id) {
        log.info("Deleting HighLevelSolution by id: {}", id);
        repository.deleteById(id);
    }

    @Override
    public List<HighLevelSolutionReadDto> findByUnsafeControlActionId(UUID unsafeControlActionId) {
        log.info("Finding HighLevelSolutions by UCA id: {}", unsafeControlActionId);
        List<UUID> classIds = formalScenarioClassRepository.findAll().stream()
                .filter(c -> c.getFormalScenario() != null && unsafeControlActionId.equals(c.getFormalScenario().getUnsafeControlActionId()))
                .map(FormalScenarioClass::getId)
                .toList();

        if (classIds.isEmpty()) return List.of();

        return repository.findByFormalScenarioClassIdIn(classIds).stream()
                .map(HighLevelSolutionMapper::toDto)
                .toList();
    }
}
