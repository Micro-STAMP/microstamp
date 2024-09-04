package microstamp.step1.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.step1.client.MicroStampClient;
import microstamp.step1.entity.Hazard;
import microstamp.step1.entity.SystemSafetyConstraint;
import microstamp.step1.dto.systemsafetyconstraint.SystemSafetyConstraintInsertDto;
import microstamp.step1.dto.systemsafetyconstraint.SystemSafetyConstraintReadDto;
import microstamp.step1.dto.systemsafetyconstraint.SystemSafetyConstraintUpdateDto;
import microstamp.step1.exception.Step1IllegalArgumentException;
import microstamp.step1.exception.Step1NotFoundException;
import microstamp.step1.mapper.SystemSafetyConstraintMapper;
import microstamp.step1.repository.HazardRepository;
import microstamp.step1.repository.SystemSafetyConstraintRepository;
import microstamp.step1.service.SystemSafetyConstraintService;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Component
@AllArgsConstructor
public class SystemSafetyConstraintServiceImpl implements SystemSafetyConstraintService {

    private final SystemSafetyConstraintRepository systemSafetyConstraintRepository;

    private final HazardRepository hazardRepository;

    private final MicroStampClient microStampClient;

    public List<SystemSafetyConstraintReadDto> findAll() {

        log.info("Find all system safety constraints");
        return systemSafetyConstraintRepository.findAll().stream()
                .map(SystemSafetyConstraintMapper::toDto)
                .sorted(Comparator.comparing(SystemSafetyConstraintReadDto::getCode))
                .toList();
    }

    public SystemSafetyConstraintReadDto findById(UUID id) throws Step1NotFoundException {

        log.info("Find system safety constraint by id: {}", id);
        return SystemSafetyConstraintMapper.toDto(systemSafetyConstraintRepository.findById(id)
                .orElseThrow(() -> new Step1NotFoundException("SystemSafetyConstraint", id.toString())));
    }

    public List<SystemSafetyConstraintReadDto> findByAnalysisId(UUID id) {

        log.info("Find system safety constraint by the analysis id: {}", id);
        return systemSafetyConstraintRepository.findByAnalysisId(id).stream()
                .map(SystemSafetyConstraintMapper::toDto)
                .sorted(Comparator.comparing(SystemSafetyConstraintReadDto::getCode))
                .toList();
    }

    public SystemSafetyConstraintReadDto insert(SystemSafetyConstraintInsertDto systemSafetyConstraintInsertDto) throws Step1NotFoundException {

        log.debug("Verifying if the system safety constraint insert is valid");
        if (Objects.isNull(systemSafetyConstraintInsertDto)) {
            throw new Step1IllegalArgumentException("Unable to create a new system safety constraint because the provided SystemSafetyConstraintInsertDto is null.");
        }

        log.info("Verifying if the analysis exists on the database");
        microStampClient.getAnalysisById(systemSafetyConstraintInsertDto.getAnalysisId());

        SystemSafetyConstraint systemSafetyConstraint = SystemSafetyConstraintMapper.toEntity(systemSafetyConstraintInsertDto);

        log.info("Finding the hazards {} associated with the system safety constraint {}", systemSafetyConstraintInsertDto.getHazardsId(), systemSafetyConstraint.getId());
        List<Hazard> hazardEntities = Optional.ofNullable(systemSafetyConstraintInsertDto.getHazardsId())
                .orElseGet(Collections::emptyList)
                .stream()
                .map(id -> hazardRepository.findById(id)
                .orElseThrow(() -> new Step1NotFoundException("Hazard", id.toString()))).toList();

        systemSafetyConstraint.setHazardEntities(hazardEntities);

        log.info("Inserting the system safety constraint {} on the database", systemSafetyConstraint);
        systemSafetyConstraintRepository.save(systemSafetyConstraint);

        return SystemSafetyConstraintMapper.toDto(systemSafetyConstraint);
    }

    public void update(UUID id, SystemSafetyConstraintUpdateDto systemSafetyConstraintUpdateDto) throws Step1NotFoundException {

        log.debug("Verifying if the system safety constraint update is valid");
        if (Objects.isNull(systemSafetyConstraintUpdateDto)) {
            throw new Step1IllegalArgumentException("Unable to update the system safety constraint because the provided SystemSafetyConstraintUpdateDto is null.");
        }

        log.debug("Finding if there is a system safety constraint with id {} to update", id);
        SystemSafetyConstraint systemSafetyConstraint = systemSafetyConstraintRepository.findById(id)
                .orElseThrow(() -> new Step1NotFoundException("SystemSafetyConstraint", id.toString()));

        systemSafetyConstraint.setName(systemSafetyConstraintUpdateDto.getName());
        systemSafetyConstraint.setCode(systemSafetyConstraintUpdateDto.getCode());

        log.info("Finding hazards {} associated with the system safety constraint {}", systemSafetyConstraintUpdateDto.getHazardsId(), systemSafetyConstraint.getId());
        List<Hazard> hazardEntities = Optional.ofNullable(systemSafetyConstraintUpdateDto.getHazardsId())
                .orElseGet(Collections::emptyList)
                .stream()
                .map(hazardId -> hazardRepository.findById(hazardId)
                        .orElseThrow(() -> new Step1NotFoundException("Hazard", hazardId.toString())))
                .collect(Collectors.toList());;
        systemSafetyConstraint.setHazardEntities(hazardEntities);

        log.info("Updating the system safety constraint with id {} setting the name {}", id, systemSafetyConstraint.getName());
        systemSafetyConstraintRepository.save(systemSafetyConstraint);
    }

    public void delete(UUID id) throws Step1NotFoundException {

        log.debug("Finding if there is a system safety constraint with id {} to delete", id);
        SystemSafetyConstraint systemSafetyConstraint = systemSafetyConstraintRepository.findById(id)
                .orElseThrow(() -> new Step1NotFoundException("SystemSafetyConstraint", id.toString()));

        log.info("Deleting the hazards associated with system safety constraint of id {} on the database", systemSafetyConstraint.getId());
        systemSafetyConstraintRepository.deleteHazardsAssociation(systemSafetyConstraint.getId().toString());

        log.info("Deleting the system safety constraint with id {} on the database", systemSafetyConstraint.getId());
        systemSafetyConstraintRepository.deleteById(systemSafetyConstraint.getId());
    }
}
