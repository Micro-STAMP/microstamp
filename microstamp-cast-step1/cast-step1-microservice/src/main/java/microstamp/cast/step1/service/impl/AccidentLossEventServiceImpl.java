package microstamp.cast.step1.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.cast.step1.client.MicroStampClient;
import microstamp.cast.step1.dto.accidentlossevent.AccidentLossEventInsertDto;
import microstamp.cast.step1.dto.accidentlossevent.AccidentLossEventReadDto;
import microstamp.cast.step1.dto.accidentlossevent.AccidentLossEventUpdateDto;
import microstamp.cast.step1.entity.AccidentLossEvent;
import microstamp.cast.step1.exception.Step1IllegalArgumentException;
import microstamp.cast.step1.exception.Step1NotFoundException;
import microstamp.cast.step1.mapper.AccidentLossEventMapper;
import microstamp.cast.step1.repository.AccidentLossEventRepository;
import microstamp.cast.step1.service.AccidentLossEventService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Log4j2
@Service
@AllArgsConstructor
public class AccidentLossEventServiceImpl implements AccidentLossEventService {

    private final AccidentLossEventRepository accidentLossEventRepository;

    //Mesma coisa das outras coisas, precisa implementar o microstampclient
    //private final MicroStampClient microStampClient;

    public List<AccidentLossEventReadDto> findAll() {
        log.info("Finding all CAST accident/loss events");
        return accidentLossEventRepository.findAll().stream()
                .map(AccidentLossEventMapper::toDto)
                .sorted(Comparator.comparing(AccidentLossEventReadDto::getCode))
                .toList();
    }

    public AccidentLossEventReadDto findById(UUID id) throws Step1NotFoundException {
        log.info("Finding CAST accident/loss event by id: {}", id);
        return AccidentLossEventMapper.toDto(accidentLossEventRepository.findById(id)
                .orElseThrow(() -> new Step1NotFoundException("AccidentLossEvent", id.toString())));
    }

    public List<AccidentLossEventReadDto> findByAnalysisId(UUID id) {
        log.info("Finding CAST accident/loss events by analysis id: {}", id);
        return accidentLossEventRepository.findByAnalysisId(id).stream()
                .map(AccidentLossEventMapper::toDto)
                .sorted(Comparator.comparing(AccidentLossEventReadDto::getCode))
                .toList();
    }

    public AccidentLossEventReadDto insert(AccidentLossEventInsertDto accidentLossEventInsertDto) throws Step1NotFoundException {
        log.debug("Verifying if the CAST accident/loss event insert is valid");
        if (Objects.isNull(accidentLossEventInsertDto)) {
            throw new Step1IllegalArgumentException("Unable to create a new accident/loss event because the provided AccidentLossEventInsertDto is null.");
        }

        log.info("Verifying if the analysis exists on the database");
        //microStampClient.getAnalysisById(accidentLossEventInsertDto.getAnalysisId());

        AccidentLossEvent accidentLossEvent = AccidentLossEventMapper.toEntity(accidentLossEventInsertDto);

        log.info("Inserting the CAST accident/loss event {} on the database", accidentLossEvent);
        accidentLossEventRepository.save(accidentLossEvent);

        return AccidentLossEventMapper.toDto(accidentLossEvent);
    }

    public void update(UUID id, AccidentLossEventUpdateDto accidentLossEventUpdateDto) throws Step1NotFoundException {
        log.debug("Verifying if the CAST accident/loss event update is valid");
        if (Objects.isNull(accidentLossEventUpdateDto)) {
            throw new Step1IllegalArgumentException("Unable to update the accident/loss event because the provided AccidentLossEventUpdateDto is null.");
        }

        log.debug("Finding if there is a CAST accident/loss event with id {} to update", id);
        AccidentLossEvent accidentLossEvent = accidentLossEventRepository.findById(id)
                .orElseThrow(() -> new Step1NotFoundException("AccidentLossEvent", id.toString()));

        accidentLossEvent.setCode(accidentLossEventUpdateDto.getCode());
        accidentLossEvent.setName(accidentLossEventUpdateDto.getName());
        accidentLossEvent.setDescription(accidentLossEventUpdateDto.getDescription());

        log.info("Updating the CAST accident/loss event with id {}", id);
        accidentLossEventRepository.save(accidentLossEvent);
    }

    @Override
    @Transactional
    public void delete(UUID id) throws Step1NotFoundException {
        log.debug("Finding if there is a CAST accident/loss event with id {} to delete", id);
        AccidentLossEvent accidentLossEvent = accidentLossEventRepository.findById(id)
                .orElseThrow(() -> new Step1NotFoundException("AccidentLossEvent", id.toString()));

        log.info("Cleaning hazard associations for accident/loss event with id {}", id);
        accidentLossEventRepository.deleteHazardAssociation(id.toString());

        log.info("Deleting the CAST accident/loss event with id {} on the database", accidentLossEvent.getId());
        accidentLossEventRepository.deleteById(accidentLossEvent.getId());
    }
}