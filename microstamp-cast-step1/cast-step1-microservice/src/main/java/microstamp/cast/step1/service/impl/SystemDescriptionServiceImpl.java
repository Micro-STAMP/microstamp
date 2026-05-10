package microstamp.cast.step1.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.cast.step1.client.MicroStampClient;
import microstamp.cast.step1.dto.systemdescription.SystemDescriptionInsertDto;
import microstamp.cast.step1.dto.systemdescription.SystemDescriptionReadDto;
import microstamp.cast.step1.dto.systemdescription.SystemDescriptionUpdateDto;
import microstamp.cast.step1.entity.SystemDescription;
import microstamp.cast.step1.exception.Step1IllegalArgumentException;
import microstamp.cast.step1.exception.Step1NotFoundException;
import microstamp.cast.step1.mapper.SystemDescriptionMapper;
import microstamp.cast.step1.repository.SystemDescriptionRepository;
import microstamp.cast.step1.service.SystemDescriptionService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

//Nesta implementação, precisaremos alterar o comportamento baseado no MicroStampClient, que ainda não foi adicionado
@Log4j2
@Service
@AllArgsConstructor
public class SystemDescriptionServiceImpl implements SystemDescriptionService {

    private final SystemDescriptionRepository systemDescriptionRepository;

    //Será adicionado um private microstampclient aqui

    @Override
    public List<SystemDescriptionReadDto> findAll() {
        log.info("Finding all CAST system descriptions");
        return systemDescriptionRepository.findAll().stream()
                .map(SystemDescriptionMapper::toDto)
                .sorted(Comparator.comparing(SystemDescriptionReadDto::getCode))
                .toList();
    }

    @Override
    public SystemDescriptionReadDto findById(UUID id) throws Step1NotFoundException {
        log.info("Finding CAST system description by id: {}", id);
        return SystemDescriptionMapper.toDto(systemDescriptionRepository.findById(id)
                .orElseThrow(() -> new Step1NotFoundException("SystemDescription", id.toString())));
    }

    @Override
    public List<SystemDescriptionReadDto> findByAnalysisId(UUID id) {
        log.info("Finding CAST system descriptions by analysis id: {}", id);
        return systemDescriptionRepository.findByAnalysisId(id).stream()
                .map(SystemDescriptionMapper::toDto)
                .sorted(Comparator.comparing(SystemDescriptionReadDto::getCode))
                .toList();
    }

    @Override
    public SystemDescriptionReadDto insert(SystemDescriptionInsertDto systemDescriptionInsertDto) throws Step1NotFoundException {
        log.debug("Verifying if the CAST system description insert is valid");
        if (Objects.isNull(systemDescriptionInsertDto)) {
            throw new Step1IllegalArgumentException("Unable to create a new system description because the provided SystemDescriptionInsertDto is null.");
        }

        log.info("Verifying if the analysis exists on the database");
        //microStampClient.getAnalysisById(systemDescriptionInsertDto.getAnalysisId());
        //Irá realizar a análise quan do o microstampclient for implementado

        SystemDescription systemDescription = SystemDescriptionMapper.toEntity(systemDescriptionInsertDto);

        log.info("Inserting the CAST system description {} on the database", systemDescription);
        systemDescriptionRepository.save(systemDescription);

        return SystemDescriptionMapper.toDto(systemDescription);
    }

    @Override
    public void update(UUID id, SystemDescriptionUpdateDto systemDescriptionUpdateDto) throws Step1NotFoundException {
        log.debug("Verifying if the CAST system description update is valid");
        if (Objects.isNull(systemDescriptionUpdateDto)) {
            throw new Step1IllegalArgumentException("Unable to update the system description because the provided SystemDescriptionUpdateDto is null.");
        }

        log.debug("Finding if there is a CAST system description with id {} to update", id);
        SystemDescription systemDescription = systemDescriptionRepository.findById(id)
                .orElseThrow(() -> new Step1NotFoundException("SystemDescription", id.toString()));

        systemDescription.setCode(systemDescriptionUpdateDto.getCode());
        systemDescription.setDescription(systemDescriptionUpdateDto.getDescription());
        systemDescription.setAnalysisBoundary(systemDescriptionUpdateDto.getAnalysisBoundary());

        log.info("Updating the CAST system description with id {}", id);
        systemDescriptionRepository.save(systemDescription);
    }

    @Override
    public void delete(UUID id) throws Step1NotFoundException {
        log.debug("Finding if there is a CAST system description with id {} to delete", id);
        SystemDescription systemDescription = systemDescriptionRepository.findById(id)
                .orElseThrow(() -> new Step1NotFoundException("SystemDescription", id.toString()));

        log.info("Deleting the CAST system description with id {} on the database", systemDescription.getId());
        systemDescriptionRepository.deleteById(systemDescription.getId());
    }
}