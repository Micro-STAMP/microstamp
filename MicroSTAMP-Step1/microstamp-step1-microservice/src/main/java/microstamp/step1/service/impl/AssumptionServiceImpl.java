package microstamp.step1.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.step1.client.MicroStampClient;
import microstamp.step1.dto.assumption.AssumptionInsertDto;
import microstamp.step1.dto.assumption.AssumptionReadDto;
import microstamp.step1.dto.assumption.AssumptionUpdateDto;
import microstamp.step1.entity.Assumption;
import microstamp.step1.exception.Step1IllegalArgumentException;
import microstamp.step1.exception.Step1NotFoundException;
import microstamp.step1.mapper.AssumptionMapper;
import microstamp.step1.repository.AssumptionRepository;
import microstamp.step1.service.AssumptionService;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Log4j2
@Component
@AllArgsConstructor
public class AssumptionServiceImpl implements AssumptionService {

    private final AssumptionRepository assumptionRepository;

    private final MicroStampClient microStampClient;

    public List<AssumptionReadDto> findAll() {
        log.info("Finding all assumptions");
        return assumptionRepository.findAll().stream()
                .map(AssumptionMapper::toDto)
                .sorted(Comparator.comparing(AssumptionReadDto::getName))
                .toList();
    }

    public AssumptionReadDto findById(UUID id) throws Step1NotFoundException {
        log.info("Finding assumption by id: {}", id);
        return AssumptionMapper.toDto(assumptionRepository.findById(id)
                .orElseThrow(() -> new Step1NotFoundException("Assumption", id.toString())));
    }

    public List<AssumptionReadDto> findByAnalysisId(UUID id) {
        log.info("Finding assumption by the analysis id: {}", id);
        return assumptionRepository.findByAnalysisId(id).stream()
                .map(AssumptionMapper::toDto)
                .sorted(Comparator.comparing(AssumptionReadDto::getName))
                .toList();
    }

    public AssumptionReadDto insert(AssumptionInsertDto assumptionInsertDto) throws Step1NotFoundException {
        log.debug("Verifying if the assumption insert is valid");
        if (Objects.isNull(assumptionInsertDto)) {
            throw new Step1IllegalArgumentException("Unable to create a new assumption because the provided AssumptionInsertDto is null.");
        }

        log.info("Verifying if the analysis exists on the database");
        microStampClient.getAnalysisById(assumptionInsertDto.getAnalysisId());

        Assumption assumption = AssumptionMapper.toEntity(assumptionInsertDto);
        log.info("Inserting the assumption {} on the database", assumption);
        assumptionRepository.save(assumption);

        return AssumptionMapper.toDto(assumption);
    }

    public void update(UUID id, AssumptionUpdateDto assumptionUpdateDto) throws Step1NotFoundException {
        log.debug("Verifying if the assumption update is valid");
        if (Objects.isNull(assumptionUpdateDto)) {
            throw new Step1IllegalArgumentException("Unable to update the assumption because the provided AssumptionUpdateDto is null.");
        }

        log.debug("Finding if there is an assumption with id {} to update", id);
        Assumption assumption = assumptionRepository.findById(id)
                .orElseThrow(() -> new Step1NotFoundException("Assumption", id.toString()));

        assumption.setName(assumptionUpdateDto.getName());
        assumption.setCode(assumptionUpdateDto.getCode());

        log.info("Updating the assumption with id {} setting the name {}", id, assumption.getName());
        assumptionRepository.save(assumption);
    }

    public void delete(UUID id) throws Step1NotFoundException {
        log.debug("Finding if there is an assumption with id {} to delete", id);
        Assumption assumption = assumptionRepository.findById(id)
                .orElseThrow(() -> new Step1NotFoundException("Assumption", id.toString()));

        log.info("Deleting the assumption with id {} on the database", assumption.getId());
        assumptionRepository.deleteById(assumption.getId());
    }
}