package microstamp.step2.service.impl;

import feign.FeignException;
import microstamp.cast.step1.dto.violatedsystemsafetyconstraint.ViolatedSystemSafetyConstraintReadDto;
import microstamp.step1.dto.systemsafetyconstraint.SystemSafetyConstraintReadDto;
import microstamp.step2.client.MicroStampCastStep1Client;
import microstamp.step2.client.MicroStampStep1Client;
import microstamp.step2.dto.responsibility.ResponsibilityReadDto;
import microstamp.step2.dto.responsibility.ResponsibilityUpdateDto;
import microstamp.step2.entity.Environment;
import microstamp.step2.entity.Responsibility;
import microstamp.step2.dto.responsibility.ResponsibilityInsertDto;
import microstamp.step2.exception.Step2EnvironmentResponsibilityException;
import microstamp.step2.exception.Step2InvalidResponsibilityConstraintException;
import microstamp.step2.exception.Step2NotFoundException;
import microstamp.step2.mapper.ResponsibilityMapper;
import microstamp.step2.repository.ComponentRepository;
import microstamp.step2.repository.ResponsibilityRepository;
import microstamp.step2.service.ResponsibilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class ResponsibilityServiceImpl implements ResponsibilityService {

    @Autowired
    private MicroStampStep1Client microStampStep1Client;

    @Autowired
    private MicroStampCastStep1Client microStampCastStep1Client;

    @Autowired
    private ResponsibilityRepository responsibilityRepository;

    @Autowired
    private ComponentRepository componentRepository;

    public List<ResponsibilityReadDto> findAll() {
        return responsibilityRepository.findAll().stream()
                .map(this::toDtoWithConstraints)
                .sorted(Comparator.comparing(ResponsibilityReadDto::getCode))
                .toList();
    }

    public ResponsibilityReadDto findById(UUID id) throws Step2NotFoundException {
        Responsibility responsibility = responsibilityRepository.findById(id)
                .orElseThrow(() -> new Step2NotFoundException("Responsibility", id.toString()));

        return toDtoWithConstraints(responsibility);
    }

    public List<ResponsibilityReadDto> findByAnalysisId(UUID id) {
        return componentRepository.findByAnalysisId(id).stream()
                .filter(c -> !c.getResponsibilities().isEmpty())
                .flatMap(c -> responsibilityRepository.findByComponentId(c.getId()).stream())
                .map(this::toDtoWithConstraints)
                .sorted(Comparator.comparing(ResponsibilityReadDto::getCode))
                .toList();
    }

    public List<ResponsibilityReadDto> findByComponentId(UUID id) {
        return responsibilityRepository.findByComponentId(id).stream()
                .map(this::toDtoWithConstraints)
                .sorted(Comparator.comparing(ResponsibilityReadDto::getCode))
                .toList();
    }

    public ResponsibilityReadDto insert(ResponsibilityInsertDto responsibilityInsertDto) throws Step2EnvironmentResponsibilityException {
        validateExactlyOneConstraint(responsibilityInsertDto.getSystemSafetyConstraintId(), responsibilityInsertDto.getViolatedSystemSafetyConstraintIds());

        microstamp.step2.entity.Component component = componentRepository.findById(responsibilityInsertDto.getComponentId())
                .orElseThrow(() -> new Step2NotFoundException("Component", responsibilityInsertDto.getComponentId().toString()));

        if (component instanceof Environment)
            throw new Step2EnvironmentResponsibilityException();

        SystemSafetyConstraintReadDto systemSafetyConstraintReadDto = responsibilityInsertDto.getSystemSafetyConstraintId() != null
                ? microStampStep1Client.getSystemSafetyConstraintById(responsibilityInsertDto.getSystemSafetyConstraintId())
                : null;

        List<ViolatedSystemSafetyConstraintReadDto> violatedSystemSafetyConstraintReadDtos = fetchViolatedConstraints(
                responsibilityInsertDto.getViolatedSystemSafetyConstraintIds());

        Responsibility responsibility = ResponsibilityMapper.toEntity(responsibilityInsertDto, component);
        responsibilityRepository.save(responsibility);

        return ResponsibilityMapper.toDto(responsibility, systemSafetyConstraintReadDto, violatedSystemSafetyConstraintReadDtos);
    }

    public void update(UUID id, ResponsibilityUpdateDto responsibilityUpdateDto) throws Step2NotFoundException {
        validateExactlyOneConstraint(responsibilityUpdateDto.getSystemSafetyConstraintId(), responsibilityUpdateDto.getViolatedSystemSafetyConstraintIds());

        Responsibility responsibility = responsibilityRepository.findById(id)
                .orElseThrow(() -> new Step2NotFoundException("Responsibility", id.toString()));

        if (responsibilityUpdateDto.getSystemSafetyConstraintId() != null)
            microStampStep1Client.getSystemSafetyConstraintById(responsibilityUpdateDto.getSystemSafetyConstraintId());
        else
            fetchViolatedConstraints(responsibilityUpdateDto.getViolatedSystemSafetyConstraintIds());

        responsibility.setResponsibility(responsibilityUpdateDto.getResponsibility());
        responsibility.setCode(responsibilityUpdateDto.getCode());
        responsibility.setSystemSafetyConstraintId(responsibilityUpdateDto.getSystemSafetyConstraintId());
        responsibility.setViolatedSystemSafetyConstraintIds(responsibilityUpdateDto.getViolatedSystemSafetyConstraintIds());

        responsibilityRepository.save(responsibility);
    }

    public void delete(UUID id) throws Step2NotFoundException {
        Responsibility responsibility = responsibilityRepository.findById(id)
                .orElseThrow(() -> new Step2NotFoundException("Responsibility", id.toString()));
        responsibilityRepository.deleteById(responsibility.getId());
    }

    private void validateExactlyOneConstraint(UUID systemSafetyConstraintId, List<UUID> violatedSystemSafetyConstraintIds) {
        boolean hasStpa = systemSafetyConstraintId != null;
        boolean hasCast = violatedSystemSafetyConstraintIds != null && !violatedSystemSafetyConstraintIds.isEmpty();
        if (hasStpa == hasCast) {
            throw new Step2InvalidResponsibilityConstraintException();
        }
    }

    private List<ViolatedSystemSafetyConstraintReadDto> fetchViolatedConstraints(List<UUID> ids) {
        if (Objects.isNull(ids))
            return List.of();

        return ids.stream()
                .map(microStampCastStep1Client::getViolatedSystemSafetyConstraintById)
                .toList();
    }

    private ResponsibilityReadDto toDtoWithConstraints(Responsibility responsibility) {
        SystemSafetyConstraintReadDto systemSafetyConstraintReadDto = fetchSystemSafetyConstraintFromExistingResponsibility(responsibility);
        List<ViolatedSystemSafetyConstraintReadDto> violatedSystemSafetyConstraintReadDtos = fetchViolatedSystemSafetyConstraintsFromExistingResponsibility(responsibility);
        return ResponsibilityMapper.toDto(responsibility, systemSafetyConstraintReadDto, violatedSystemSafetyConstraintReadDtos);
    }

    private SystemSafetyConstraintReadDto fetchSystemSafetyConstraintFromExistingResponsibility(Responsibility responsibility) {
        if (Objects.isNull(responsibility.getSystemSafetyConstraintId()))
            return null;

        try {
            return microStampStep1Client.getSystemSafetyConstraintById(responsibility.getSystemSafetyConstraintId());
        } catch (FeignException.FeignClientException ex) {
            handleSystemSafetyConstraintNotFound(ex, responsibility);
            return null;
        }
    }

    private List<ViolatedSystemSafetyConstraintReadDto> fetchViolatedSystemSafetyConstraintsFromExistingResponsibility(Responsibility responsibility) {
        if (Objects.isNull(responsibility.getViolatedSystemSafetyConstraintIds()) || responsibility.getViolatedSystemSafetyConstraintIds().isEmpty())
            return List.of();

        List<UUID> stillValidIds = new ArrayList<>(responsibility.getViolatedSystemSafetyConstraintIds());
        List<ViolatedSystemSafetyConstraintReadDto> found = new ArrayList<>();

        for (UUID constraintId : responsibility.getViolatedSystemSafetyConstraintIds()) {
            try {
                found.add(microStampCastStep1Client.getViolatedSystemSafetyConstraintById(constraintId));
            } catch (FeignException.FeignClientException ex) {
                if (ex.getMessage().contains("Step1NotFoundException")) {
                    stillValidIds.remove(constraintId);
                } else {
                    throw ex;
                }
            }
        }

        if (stillValidIds.size() != responsibility.getViolatedSystemSafetyConstraintIds().size()) {
            responsibility.setViolatedSystemSafetyConstraintIds(stillValidIds);
            responsibilityRepository.save(responsibility);
        }

        return found;
    }

    private void handleSystemSafetyConstraintNotFound(FeignException.FeignClientException ex, Responsibility responsibility) {
        if (ex.getMessage().contains("Step1NotFoundException")) {
            responsibility.setSystemSafetyConstraintId(null);
            responsibilityRepository.save(responsibility);
        } else {
            throw ex;
        }
    }
}
