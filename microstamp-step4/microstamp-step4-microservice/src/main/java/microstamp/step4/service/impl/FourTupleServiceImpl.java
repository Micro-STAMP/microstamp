package microstamp.step4.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.step4.client.MicroStampAuthClient;
import microstamp.step4.dto.fourtuple.FourTupleInsertDto;
import microstamp.step4.dto.fourtuple.FourTupleReadDto;
import microstamp.step4.dto.fourtuple.FourTupleUpdateDto;
import microstamp.step4.entity.FourTuple;
import microstamp.step4.exception.Step4IllegalArgumentException;
import microstamp.step4.exception.Step4NotFoundException;
import microstamp.step4.mapper.FourTupleMapper;
import microstamp.step4.repository.FourTupleRepository;
import microstamp.step4.service.FourTupleService;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Log4j2
@Component
@AllArgsConstructor
public class FourTupleServiceImpl implements FourTupleService {

    private final FourTupleRepository fourTupleRepository;

    private final MicroStampAuthClient microStampAuthClient;

    public List<FourTupleReadDto> findAll() {
        log.info("Finding all 4-tuples");
        return fourTupleRepository.findAll().stream()
                .map(FourTupleMapper::toDto)
                .sorted(Comparator.comparing(FourTupleReadDto::getCode))
                .toList();
    }

    public FourTupleReadDto findById(UUID id) throws Step4NotFoundException {
        log.info("Finding 4-tuple by id: {}", id);
        return FourTupleMapper.toDto(fourTupleRepository.findById(id)
                .orElseThrow(() -> new Step4NotFoundException("4-tuple", id.toString())));
    }

    public List<FourTupleReadDto> findByAnalysisId(UUID id) {
        log.info("Finding 4-tuple by the analysis id: {}", id);
        return fourTupleRepository.findByAnalysisId(id).stream()
                .map(FourTupleMapper::toDto)
                .sorted(Comparator.comparing(FourTupleReadDto::getCode))
                .toList();
    }

    public FourTupleReadDto insert(FourTupleInsertDto fourTupleInsertDto) throws Step4NotFoundException {
        log.debug("Verifying if the 4-tuple insert is valid");
        if (Objects.isNull(fourTupleInsertDto)) {
            throw new Step4IllegalArgumentException("Unable to create a new 4-tuple because the provided FourTupleInsertDto is null.");
        }

        log.info("Verifying if the analysis exists on the database");
        microStampAuthClient.getAnalysisById(fourTupleInsertDto.getAnalysisId());

        //TODO check if the list of provided unsafe control actions exists in step 3

        FourTuple fourTuple = FourTupleMapper.toEntity(fourTupleInsertDto);
        log.info("Inserting the 4-tuple {} on the database", fourTuple);
        fourTupleRepository.save(fourTuple);

        return FourTupleMapper.toDto(fourTuple);
    }

    public void update(UUID id, FourTupleUpdateDto fourTupleUpdateDto) throws Step4NotFoundException {
        log.debug("Verifying if the 4-tuple update is valid");
        if (Objects.isNull(fourTupleUpdateDto)) {
            throw new Step4IllegalArgumentException("Unable to create a new 4-tuple because the provided FourTupleUpdateDto is null.");
        }

        log.debug("Finding if there is a 4-tuple with id {} to update", id);
        FourTuple fourTuple = fourTupleRepository.findById(id)
                .orElseThrow(() -> new Step4NotFoundException("4-tuple", id.toString()));

        //TODO check if the list of provided unsafe control actions exists in step 3

        fourTuple.setCode(fourTupleUpdateDto.getCode());
        fourTuple.setScenario(fourTupleUpdateDto.getScenario());
        fourTuple.setAssociatedCausalFactor(fourTupleUpdateDto.getAssociatedCausalFactor());
        fourTuple.setRecommendation(fourTupleUpdateDto.getRecommendation());
        fourTuple.setRationale(fourTupleUpdateDto.getRationale());
        fourTuple.setUnsafeControlActions(fourTupleUpdateDto.getUnsafeControlActionIds());

        log.info("Updating the 4-tuple with id {}", id);
        fourTupleRepository.save(fourTuple);
    }

    public void delete(UUID id) throws Step4NotFoundException {
        log.debug("Finding if there is a 4-tuple with id {} to delete", id);
        FourTuple fourTuple = fourTupleRepository.findById(id)
                .orElseThrow(() -> new Step4NotFoundException("4-tuple", id.toString()));

        log.info("Deleting the 4-tuple with id {} on the database", fourTuple.getId());
        fourTupleRepository.deleteById(id);
    }
}
