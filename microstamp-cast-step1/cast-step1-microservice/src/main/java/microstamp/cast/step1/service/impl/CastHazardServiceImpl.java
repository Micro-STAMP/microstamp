package microstamp.cast.step1.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.cast.step1.dto.casthazard.CastHazardInsertDto;
import microstamp.cast.step1.dto.casthazard.CastHazardReadDto;
import microstamp.cast.step1.dto.casthazard.CastHazardUpdateDto;
import microstamp.cast.step1.entity.AccidentLossEvent;
import microstamp.cast.step1.entity.CastHazard;
import microstamp.cast.step1.exception.Step1IllegalArgumentException;
import microstamp.cast.step1.exception.Step1NotFoundException;
import microstamp.cast.step1.mapper.CastHazardMapper;
import microstamp.cast.step1.repository.AccidentLossEventRepository;
import microstamp.cast.step1.repository.CastHazardRepository;
import microstamp.cast.step1.service.CastHazardService;
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
public class CastHazardServiceImpl implements CastHazardService {

    private final CastHazardRepository castHazardRepository;

    private final AccidentLossEventRepository accidentLossEventRepository;

    //private final MicroStampClient microStampClient;

    public List<CastHazardReadDto> findAll() {
        log.info("Finding all CAST hazards");
        return castHazardRepository.findAll().stream()
                .map(CastHazardMapper::toDto)
                .sorted(Comparator.comparing(CastHazardReadDto::getCode))
                .toList();
    }

    public CastHazardReadDto findById(UUID id) throws Step1NotFoundException {
        log.info("Finding CAST hazard by id: {}", id);
        return CastHazardMapper.toDto(castHazardRepository.findById(id)
                .orElseThrow(() -> new Step1NotFoundException("CastHazard", id.toString())));
    }

    public List<CastHazardReadDto> findByAnalysisId(UUID id) {
        log.info("Finding CAST hazards by analysis id: {}", id);
        return castHazardRepository.findByAnalysisId(id).stream()
                .map(CastHazardMapper::toDto)
                .sorted(Comparator.comparing(CastHazardReadDto::getCode))
                .toList();
    }

    public CastHazardReadDto insert(CastHazardInsertDto castHazardInsertDto) throws Step1NotFoundException {
        log.debug("Verifying if the CAST hazard insert is valid");
        if (Objects.isNull(castHazardInsertDto)) {
            throw new Step1IllegalArgumentException("Unable to create a new CAST hazard because the provided CastHazardInsertDto is null.");
        }

        log.info("Verifying if the analysis exists on the database");
        //microStampClient.getAnalysisById(castHazardInsertDto.getAnalysisId());

        CastHazard castHazard = CastHazardMapper.toEntity(castHazardInsertDto);

        log.debug("Finding accident/loss events {} associated with the CAST hazard", castHazardInsertDto.getAccidentLossEventIds());
        List<AccidentLossEvent> accidentLossEvents = getIdsOrEmptyList(castHazardInsertDto.getAccidentLossEventIds()).stream()
                .map(eventId -> accidentLossEventRepository.findById(eventId)
                        .orElseThrow(() -> new Step1NotFoundException("AccidentLossEvent", eventId.toString())))
                .toList();

        castHazard.setAccidentLossEvents(accidentLossEvents);

        log.info("Inserting the CAST hazard {} on the database", castHazard);
        castHazardRepository.save(castHazard);

        return CastHazardMapper.toDto(castHazard);
    }

    public void update(UUID id, CastHazardUpdateDto castHazardUpdateDto) throws Step1NotFoundException {
        log.debug("Verifying if the CAST hazard update is valid");
        if (Objects.isNull(castHazardUpdateDto)) {
            throw new Step1IllegalArgumentException("Unable to update the CAST hazard because the provided CastHazardUpdateDto is null.");
        }

        log.debug("Finding if there is a CAST hazard with id {} to update", id);
        CastHazard castHazard = castHazardRepository.findById(id)
                .orElseThrow(() -> new Step1NotFoundException("CastHazard", id.toString()));

        castHazard.setCode(castHazardUpdateDto.getCode());
        castHazard.setName(castHazardUpdateDto.getName());
        castHazard.setDescription(castHazardUpdateDto.getDescription());

        List<AccidentLossEvent> accidentLossEvents = getIdsOrEmptyList(castHazardUpdateDto.getAccidentLossEventIds()).stream()
                .map(eventId -> accidentLossEventRepository.findById(eventId)
                        .orElseThrow(() -> new Step1NotFoundException("AccidentLossEvent", eventId.toString())))
                .collect(Collectors.toList());

        castHazard.setAccidentLossEvents(accidentLossEvents);

        log.info("Updating the CAST hazard with id {}", id);
        castHazardRepository.save(castHazard);
    }

    @Override
    @Transactional
    public void delete(UUID id) throws Step1NotFoundException {
        log.debug("Finding if there is a CAST hazard with id {} to delete", id);
        CastHazard castHazard = castHazardRepository.findById(id)
                .orElseThrow(() -> new Step1NotFoundException("CastHazard", id.toString()));

        log.info("Cleaning accident/loss event associations for hazard with id {}", id);
        castHazardRepository.deleteAccidentLossEventAssociation(id.toString());

        log.info("Cleaning violated constraint associations for hazard with id {}", id);
        castHazardRepository.deleteViolatedConstraintAssociation(id.toString());

        log.info("Deleting the CAST hazard with id {} on the database", castHazard.getId());
        castHazardRepository.deleteById(castHazard.getId());
    }

    private List<UUID> getIdsOrEmptyList(List<UUID> ids) {
        return ids != null ? ids : Collections.emptyList();
    }
}