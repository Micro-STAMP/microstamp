package microstamp.step1.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.step1.client.MicroStampClient;
import microstamp.step1.entity.SystemGoal;
import microstamp.step1.dto.systemgoal.SystemGoalInsertDto;
import microstamp.step1.dto.systemgoal.SystemGoalReadDto;
import microstamp.step1.dto.systemgoal.SystemGoalUpdateDto;
import microstamp.step1.exception.Step1IllegalArgumentException;
import microstamp.step1.exception.Step1NotFoundException;
import microstamp.step1.mapper.SystemGoalMapper;
import microstamp.step1.repository.SystemGoalRepository;
import microstamp.step1.service.SystemGoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Log4j2
@Component
@AllArgsConstructor
public class SystemGoalServiceImpl implements SystemGoalService {


    private final SystemGoalRepository systemGoalRepository;

    private final MicroStampClient microStampClient;

    public List<SystemGoalReadDto> findAll() {

        log.info("Find all system goals");
        return systemGoalRepository.findAll().stream()
                .map(SystemGoalMapper::toDto)
                .sorted(Comparator.comparing(SystemGoalReadDto::getName))
                .toList();
    }

    public SystemGoalReadDto findById(UUID id) throws Step1NotFoundException {

        log.info("Find system goal by id: {}", id);
        return SystemGoalMapper.toDto(systemGoalRepository.findById(id)
                .orElseThrow(() -> new Step1NotFoundException("SystemGoal", id.toString())));
    }

    public List<SystemGoalReadDto> findByAnalysisId(UUID id) {

        log.info("Finding system goal by the analysis id: {}", id);
        return systemGoalRepository.findByAnalysisId(id).stream()
                .map(SystemGoalMapper::toDto)
                .sorted(Comparator.comparing(SystemGoalReadDto::getName))
                .toList();
    }

    public SystemGoalReadDto insert(SystemGoalInsertDto systemGoalInsertDto) throws Step1NotFoundException {

        log.debug("Verifying if the system goal insert is valid");
        if (Objects.isNull(systemGoalInsertDto)) {
            throw new Step1IllegalArgumentException("Unable to create a new system goal because the provided SystemGoalInsertDto is null.");
        }

        log.info("Verifying if the analysis exists on the database");
        microStampClient.getAnalysisById(systemGoalInsertDto.getAnalysisId());

        SystemGoal systemGoal = SystemGoalMapper.toEntity(systemGoalInsertDto);
        log.info("Inserting the system goal {} on the database", systemGoal);
        systemGoalRepository.save(systemGoal);

        return SystemGoalMapper.toDto(systemGoal);
    }

    public void update(UUID id, SystemGoalUpdateDto systemGoalUpdateDto) throws Step1NotFoundException {

        log.debug("Verifying if the system goal insert is valid");
        if (Objects.isNull(systemGoalUpdateDto)) {
            throw new Step1IllegalArgumentException("Unable to update the system goal because the provided SystemGoalUpdateDto is null.");
        }

        log.debug("Finding if there is a system goal with id {} to update", id);
        SystemGoal systemGoal = systemGoalRepository.findById(id)
                .orElseThrow(() -> new Step1NotFoundException("SystemGoal", id.toString()));

        systemGoal.setName(systemGoalUpdateDto.getName());
        systemGoal.setCode(systemGoalUpdateDto.getCode());

        log.info("Updating the system goal with id {} setting the name {}", id, systemGoal.getName());
        systemGoalRepository.save(systemGoal);
    }

    public void delete(UUID id) throws Step1NotFoundException {

        log.debug("Finding if there is a system goal with id {} to delete", id);
        SystemGoal systemGoal = systemGoalRepository.findById(id)
                .orElseThrow(() -> new Step1NotFoundException("SystemGoal", id.toString()));

        log.info("Deleting the system goal with id {} on the database", systemGoal.getId());
        systemGoalRepository.deleteById(systemGoal.getId());
    }
}
