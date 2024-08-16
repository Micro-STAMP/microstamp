package microstamp.step1.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.step1.client.MicroStampClient;
import microstamp.step1.dto.loss.LossInsertDto;
import microstamp.step1.dto.loss.LossReadDto;
import microstamp.step1.dto.loss.LossUpdateDto;
import microstamp.step1.entity.Loss;
import microstamp.step1.exception.Step1IllegalArgumentException;
import microstamp.step1.exception.Step1NotFoundException;
import microstamp.step1.mapper.LossMapper;
import microstamp.step1.repository.LossRepository;
import microstamp.step1.service.LossService;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Log4j2
@Component
@AllArgsConstructor
public class LossServiceImpl implements LossService {


    private final LossRepository lossRepository;

    private final MicroStampClient microStampClient;

    public List<LossReadDto> findAll() {

        log.info("Find all losses");
        return lossRepository.findAll().stream()
                .map(LossMapper::toDto)
                .sorted(Comparator.comparing(LossReadDto::getName))
                .toList();
    }

    public LossReadDto findById(UUID id) throws Step1NotFoundException {

        log.info("Finding loss by id: {}", id);
        return LossMapper.toDto(lossRepository.findById(id)
                .orElseThrow(() -> new Step1NotFoundException("Loss", id.toString())));
    }

    public List<LossReadDto> findByAnalysisId(UUID id) {

        log.info("Finding loss by the analysis id: {}", id);
        return lossRepository.findByAnalysisId(id).stream()
                .map(LossMapper::toDto)
                .sorted(Comparator.comparing(LossReadDto::getName))
                .toList();
    }

    public LossReadDto insert(LossInsertDto lossInsertDto) throws Step1NotFoundException {

        log.debug("Verifying if the loss insert is valid");
        if (Objects.isNull(lossInsertDto)) {
            throw new Step1IllegalArgumentException("Unable to create a new loss because the provided LossInsertDto is null.");
        }

        log.info("Verifying if the analysis exists on the database");
        microStampClient.getAnalysisById(lossInsertDto.getAnalysisId());

        Loss loss = LossMapper.toEntity(lossInsertDto);
        log.info("Inserting the loss {} on the database", loss);
        lossRepository.save(loss);

        return LossMapper.toDto(loss);
    }

    public void update(UUID id, LossUpdateDto lossUpdateDto) throws Step1NotFoundException {

        log.debug("Verifying if the loss update is valid");
        if (Objects.isNull(lossUpdateDto)) {
            throw new Step1IllegalArgumentException("Unable to update the loss because the provided LossUpdateDto is null.");
        }

        log.debug("Finding if there is a loss with id {} to update", id);
        Loss loss = lossRepository.findById(id)
                .orElseThrow(() -> new Step1NotFoundException("Loss", id.toString()));

        loss.setName(lossUpdateDto.getName());

        log.info("Updating the loss with id {} setting the name {}", id, loss.getName());
        lossRepository.save(loss);
    }

    public void delete(UUID id) throws Step1NotFoundException {

        log.debug("Finding if there is a loss with id {} to delete", id);
        Loss loss = lossRepository.findById(id)
                .orElseThrow(() -> new Step1NotFoundException("Loss", id.toString()));

        log.info("Deleting the hazards associated with the loss {} on the database", loss.getId());
        lossRepository.deleteHazardsAssociation(id.toString());

        log.info("Deleting the loss with id {} on the database", loss.getId());
        lossRepository.deleteById(loss.getId());
    }
}
