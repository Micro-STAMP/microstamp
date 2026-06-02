package microstamp.cast.step1.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.cast.step1.client.MicroStampClient;
import microstamp.cast.step1.dto.violatedsystemsafetyconstraint.ViolatedSystemSafetyConstraintInsertDto;
import microstamp.cast.step1.dto.violatedsystemsafetyconstraint.ViolatedSystemSafetyConstraintReadDto;
import microstamp.cast.step1.dto.violatedsystemsafetyconstraint.ViolatedSystemSafetyConstraintUpdateDto;
import microstamp.cast.step1.entity.CastHazard;
import microstamp.cast.step1.entity.ViolatedSystemSafetyConstraint;
import microstamp.cast.step1.exception.Step1IllegalArgumentException;
import microstamp.cast.step1.exception.Step1NotFoundException;
import microstamp.cast.step1.mapper.ViolatedSystemSafetyConstraintMapper;
import microstamp.cast.step1.repository.CastHazardRepository;
import microstamp.cast.step1.repository.ViolatedSystemSafetyConstraintRepository;
import microstamp.cast.step1.service.ViolatedSystemSafetyConstraintService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@Service
@AllArgsConstructor
public class ViolatedSystemSafetyConstraintServiceImpl implements ViolatedSystemSafetyConstraintService {

    private final ViolatedSystemSafetyConstraintRepository violatedSystemSafetyConstraintRepository;

    private final CastHazardRepository castHazardRepository;

    //private final MicroStampClient microStampClient;

    public List<ViolatedSystemSafetyConstraintReadDto> findAll() {
        log.info("Finding all CAST violated system safety constraints");
        return violatedSystemSafetyConstraintRepository.findAll().stream()
                .map(ViolatedSystemSafetyConstraintMapper::toDto)
                .sorted(Comparator.comparing(ViolatedSystemSafetyConstraintReadDto::getCode))
                .toList();
    }

    public ViolatedSystemSafetyConstraintReadDto findById(UUID id) throws Step1NotFoundException {
        log.info("Finding CAST violated system safety constraint by id: {}", id);
        return ViolatedSystemSafetyConstraintMapper.toDto(violatedSystemSafetyConstraintRepository.findById(id)
                .orElseThrow(() -> new Step1NotFoundException("ViolatedSystemSafetyConstraint", id.toString())));
    }

    public List<ViolatedSystemSafetyConstraintReadDto> findByAnalysisId(UUID id) {
        log.info("Finding CAST violated system safety constraints by analysis id: {}", id);
        return violatedSystemSafetyConstraintRepository.findByAnalysisId(id).stream()
                .map(ViolatedSystemSafetyConstraintMapper::toDto)
                .sorted(Comparator.comparing(ViolatedSystemSafetyConstraintReadDto::getCode))
                .toList();
    }

    public ViolatedSystemSafetyConstraintReadDto insert(ViolatedSystemSafetyConstraintInsertDto dto) throws Step1NotFoundException {
        log.debug("Verifying if the CAST violated system safety constraint insert is valid");
        if (Objects.isNull(dto)) {
            throw new Step1IllegalArgumentException("Unable to create a new violated system safety constraint because the provided ViolatedSystemSafetyConstraintInsertDto is null.");
        }

        log.info("Verifying if the analysis exists on the database");
        //microStampClient.getAnalysisById(dto.getAnalysisId());

        ViolatedSystemSafetyConstraint violatedConstraint = ViolatedSystemSafetyConstraintMapper.toEntity(dto);

        log.debug("Finding CAST hazards {} associated with the violated system safety constraint", dto.getHazardIds());
        List<CastHazard> hazards = getIdsOrEmptyList(dto.getHazardIds()).stream()
                .map(hazardId -> castHazardRepository.findById(hazardId)
                        .orElseThrow(() -> new Step1NotFoundException("CastHazard", hazardId.toString())))
                .toList();

        violatedConstraint.setHazards(hazards);

        log.info("Inserting the CAST violated system safety constraint {} on the database", violatedConstraint);
        violatedSystemSafetyConstraintRepository.save(violatedConstraint);

        return ViolatedSystemSafetyConstraintMapper.toDto(violatedConstraint);
    }

    public void update(UUID id, ViolatedSystemSafetyConstraintUpdateDto dto) throws Step1NotFoundException {
        log.debug("Verifying if the CAST violated system safety constraint update is valid");
        if (Objects.isNull(dto)) {
            throw new Step1IllegalArgumentException("Unable to update the violated system safety constraint because the provided ViolatedSystemSafetyConstraintUpdateDto is null.");
        }

        log.debug("Finding if there is a CAST violated system safety constraint with id {} to update", id);
        ViolatedSystemSafetyConstraint violatedConstraint = violatedSystemSafetyConstraintRepository.findById(id)
                .orElseThrow(() -> new Step1NotFoundException("ViolatedSystemSafetyConstraint", id.toString()));

        violatedConstraint.setCode(dto.getCode());
        violatedConstraint.setName(dto.getName());
        violatedConstraint.setDescription(dto.getDescription());

        List<CastHazard> hazards = getIdsOrEmptyList(dto.getHazardIds()).stream()
                .map(hazardId -> castHazardRepository.findById(hazardId)
                        .orElseThrow(() -> new Step1NotFoundException("CastHazard", hazardId.toString())))
                .collect(Collectors.toList());

        violatedConstraint.setHazards(hazards);

        log.info("Updating the CAST violated system safety constraint with id {}", id);
        violatedSystemSafetyConstraintRepository.save(violatedConstraint);
    }

    @Override
    @Transactional
    public void delete(UUID id) throws Step1NotFoundException {
        log.debug("Finding if there is a CAST violated system safety constraint with id {} to delete", id);
        ViolatedSystemSafetyConstraint violatedConstraint = violatedSystemSafetyConstraintRepository.findById(id)
                .orElseThrow(() -> new Step1NotFoundException("ViolatedSystemSafetyConstraint", id.toString()));

        log.info("Cleaning hazard associations for violated system safety constraint with id {}", id);
        violatedSystemSafetyConstraintRepository.deleteHazardAssociation(id.toString());

        log.info("Deleting the CAST violated system safety constraint with id {} on the database", violatedConstraint.getId());
        violatedSystemSafetyConstraintRepository.deleteById(violatedConstraint.getId());
    }

    private List<UUID> getIdsOrEmptyList(List<UUID> ids) {
        return ids != null ? ids : Collections.emptyList();
    }
}