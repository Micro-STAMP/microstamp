package microstamp.cast.step1.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.cast.step1.client.MicroStampClient;
import microstamp.cast.step1.dto.physicallossanalysis.PhysicalLossAnalysisInsertDto;
import microstamp.cast.step1.dto.physicallossanalysis.PhysicalLossAnalysisReadDto;
import microstamp.cast.step1.dto.physicallossanalysis.PhysicalLossAnalysisUpdateDto;
import microstamp.cast.step1.entity.PhysicalLossAnalysis;
import microstamp.cast.step1.exception.Step1IllegalArgumentException;
import microstamp.cast.step1.exception.Step1NotFoundException;
import microstamp.cast.step1.mapper.PhysicalLossAnalysisMapper;
import microstamp.cast.step1.repository.PhysicalLossAnalysisRepository;
import microstamp.cast.step1.service.PhysicalLossAnalysisService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Log4j2
@Service
@AllArgsConstructor
public class PhysicalLossAnalysisServiceImpl implements PhysicalLossAnalysisService {

    private final PhysicalLossAnalysisRepository physicalLossAnalysisRepository;

    //private final MicroStampClient microStampClient;

    public List<PhysicalLossAnalysisReadDto> findAll() {
        log.info("Finding all CAST physical loss analyses");
        return physicalLossAnalysisRepository.findAll().stream()
                .map(PhysicalLossAnalysisMapper::toDto)
                .sorted(Comparator.comparing(PhysicalLossAnalysisReadDto::getCode))
                .toList();
    }

    public PhysicalLossAnalysisReadDto findById(UUID id) throws Step1NotFoundException {
        log.info("Finding CAST physical loss analysis by id: {}", id);
        return PhysicalLossAnalysisMapper.toDto(physicalLossAnalysisRepository.findById(id)
                .orElseThrow(() -> new Step1NotFoundException("PhysicalLossAnalysis", id.toString())));
    }

    public List<PhysicalLossAnalysisReadDto> findByAnalysisId(UUID id) {
        log.info("Finding CAST physical loss analyses by analysis id: {}", id);
        return physicalLossAnalysisRepository.findByAnalysisId(id).stream()
                .map(PhysicalLossAnalysisMapper::toDto)
                .sorted(Comparator.comparing(PhysicalLossAnalysisReadDto::getCode))
                .toList();
    }

    public PhysicalLossAnalysisReadDto insert(PhysicalLossAnalysisInsertDto physicalLossAnalysisInsertDto) throws Step1NotFoundException {
        log.debug("Verifying if the CAST physical loss analysis insert is valid");
        if (Objects.isNull(physicalLossAnalysisInsertDto)) {
            throw new Step1IllegalArgumentException("Unable to create a new physical loss analysis because the provided PhysicalLossAnalysisInsertDto is null.");
        }

        log.info("Verifying if the analysis exists on the database");
        //microStampClient.getAnalysisById(physicalLossAnalysisInsertDto.getAnalysisId());

        PhysicalLossAnalysis physicalLossAnalysis = PhysicalLossAnalysisMapper.toEntity(physicalLossAnalysisInsertDto);

        log.info("Inserting the CAST physical loss analysis {} on the database", physicalLossAnalysis);
        physicalLossAnalysisRepository.save(physicalLossAnalysis);

        return PhysicalLossAnalysisMapper.toDto(physicalLossAnalysis);
    }

    public void update(UUID id, PhysicalLossAnalysisUpdateDto physicalLossAnalysisUpdateDto) throws Step1NotFoundException {
        log.debug("Verifying if the CAST physical loss analysis update is valid");
        if (Objects.isNull(physicalLossAnalysisUpdateDto)) {
            throw new Step1IllegalArgumentException("Unable to update the physical loss analysis because the provided PhysicalLossAnalysisUpdateDto is null.");
        }

        log.debug("Finding if there is a CAST physical loss analysis with id {} to update", id);
        PhysicalLossAnalysis physicalLossAnalysis = physicalLossAnalysisRepository.findById(id)
                .orElseThrow(() -> new Step1NotFoundException("PhysicalLossAnalysis", id.toString()));

        physicalLossAnalysis.setCode(physicalLossAnalysisUpdateDto.getCode());
        physicalLossAnalysis.setPhysicalLossDescription(physicalLossAnalysisUpdateDto.getPhysicalLossDescription());
        physicalLossAnalysis.setAffectedEquipment(physicalLossAnalysisUpdateDto.getAffectedEquipment());
        physicalLossAnalysis.setPhysicalDesignRequirements(physicalLossAnalysisUpdateDto.getPhysicalDesignRequirements());
        physicalLossAnalysis.setPhysicalControls(physicalLossAnalysisUpdateDto.getPhysicalControls());
        physicalLossAnalysis.setFailuresAndUnsafeInteractions(physicalLossAnalysisUpdateDto.getFailuresAndUnsafeInteractions());
        physicalLossAnalysis.setMissingOrInadequateControls(physicalLossAnalysisUpdateDto.getMissingOrInadequateControls());
        physicalLossAnalysis.setContextualFactors(physicalLossAnalysisUpdateDto.getContextualFactors());

        log.info("Updating the CAST physical loss analysis with id {}", id);
        physicalLossAnalysisRepository.save(physicalLossAnalysis);
    }

    public void delete(UUID id) throws Step1NotFoundException {
        log.debug("Finding if there is a CAST physical loss analysis with id {} to delete", id);
        PhysicalLossAnalysis physicalLossAnalysis = physicalLossAnalysisRepository.findById(id)
                .orElseThrow(() -> new Step1NotFoundException("PhysicalLossAnalysis", id.toString()));

        log.info("Deleting the CAST physical loss analysis with id {} on the database", physicalLossAnalysis.getId());
        physicalLossAnalysisRepository.deleteById(physicalLossAnalysis.getId());
    }
}