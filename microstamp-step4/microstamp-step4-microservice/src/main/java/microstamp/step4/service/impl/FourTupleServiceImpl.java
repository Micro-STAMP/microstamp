package microstamp.step4.service.impl;

import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.step3.dto.unsafecontrolaction.UnsafeControlActionReadDto;
import microstamp.step4.client.MicroStampAuthClient;
import microstamp.step4.client.MicroStampStep3Client;
import microstamp.step4.dto.fourtuple.FourTupleInsertDto;
import microstamp.step4.dto.fourtuple.FourTupleFullReadDto;
import microstamp.step4.dto.fourtuple.FourTupleReadDto;
import microstamp.step4.dto.fourtuple.FourTupleUpdateDto;
import microstamp.step4.dto.unsafecontrolaction.UnsafeControlActionFullReadDto;
import microstamp.step4.entity.FourTuple;
import microstamp.step4.exception.Step4IllegalArgumentException;
import microstamp.step4.exception.Step4NotFoundException;
import microstamp.step4.mapper.FourTupleMapper;
import microstamp.step4.mapper.UnsafeControlActionMapper;
import microstamp.step4.repository.FourTupleRepository;
import microstamp.step4.service.FourTupleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@Component
@AllArgsConstructor
public class FourTupleServiceImpl implements FourTupleService {

    private final FourTupleRepository fourTupleRepository;

    private final MicroStampAuthClient microStampAuthClient;

    private final MicroStampStep3Client microStampStep3Client;

    public List<FourTupleFullReadDto> findAll() {
        log.info("Finding all 4-tuples");
        return fourTupleRepository.findAll().stream()
                .map(f -> FourTupleMapper.toFullDto(f, fetchUCAsFromExistingFourTuple(f)))
                .sorted(Comparator.comparing(FourTupleFullReadDto::getCode))
                .toList();
    }

    public FourTupleFullReadDto findById(UUID id) throws Step4NotFoundException {
        log.info("Finding 4-tuple by id: {}", id);
        FourTuple fourTuple = fourTupleRepository.findById(id)
                .orElseThrow(() -> new Step4NotFoundException("4-tuple", id.toString()));

        return FourTupleMapper.toFullDto(fourTuple, fetchUCAsFromExistingFourTuple(fourTuple));
    }

    public List<FourTupleFullReadDto> findByAnalysisId(UUID id) {
        log.info("Finding 4-tuple by the analysis id: {}", id);
        return fourTupleRepository.findByAnalysisId(id).stream()
                .map(f -> FourTupleMapper.toFullDto(f, fetchUCAsFromExistingFourTuple(f)))
                .sorted(Comparator.comparing(FourTupleFullReadDto::getCode))
                .toList();
    }

    public Page<FourTupleFullReadDto> findByAnalysisId(UUID id, int page, int size) {
        log.info("Finding 4-tuple by the analysis id: {} with pagination", id);

        Pageable pageable = Pageable.ofSize(size).withPage(page);
        return fourTupleRepository
                .findByAnalysisIdOrderByCode(id, pageable)
                .map(f -> FourTupleMapper.toFullDto(f, fetchUCAsFromExistingFourTuple(f)));
    }

    public List<UnsafeControlActionFullReadDto> findByAnalysisIdSortedByUnsafeControlActions(UUID analysisId) {
        log.info("Finding all UCAs and 4-tuples by analysis id {}", analysisId);

        List<UnsafeControlActionReadDto> ucaDtos = microStampStep3Client.readAllUCAByAnalysisId(analysisId);

        List<FourTupleReadDto> fourTuples = fourTupleRepository.findByAnalysisId(analysisId).stream()
                .map(f -> FourTupleMapper.toDto(f, f.getUnsafeControlActions()))
                .sorted(Comparator.comparing(FourTupleReadDto::getCode))
                .toList();

        Map<UUID, List<FourTupleReadDto>> ucaIdToFourTuples = fourTuples.stream()
                .flatMap(tuple -> {
                    List<UUID> unsafeControlActionIds = tuple.getUnsafeControlActionIds();
                    return unsafeControlActionIds == null || unsafeControlActionIds.isEmpty()
                            ? Stream.empty()
                            : unsafeControlActionIds.stream().map(ucaId -> Map.entry(ucaId, tuple));
                })
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                ));

        return ucaDtos.stream()
                .map(uca -> UnsafeControlActionMapper
                        .toFullDto(uca, ucaIdToFourTuples.getOrDefault(uca.getId(), List.of())))
                .toList();
    }

    public UnsafeControlActionFullReadDto findByUcaId(UUID ucaId) {
        log.info("Finding 4-tuples by unsafe control action id {}", ucaId);

        List<FourTupleReadDto> fourTuples = fourTupleRepository.findByUnsafeControlActionsIs(ucaId).stream()
                .map(f -> FourTupleMapper.toDto(f, f.getUnsafeControlActions()))
                .sorted(Comparator.comparing(FourTupleReadDto::getCode))
                .toList();

        UnsafeControlActionReadDto uca = microStampStep3Client.readUnsafeControlAction(ucaId);

        return UnsafeControlActionMapper.toFullDto(uca, fourTuples);
    }

    public FourTupleFullReadDto insert(FourTupleInsertDto fourTupleInsertDto) throws Step4NotFoundException {
        log.debug("Verifying if the 4-tuple insert is valid");
        if (Objects.isNull(fourTupleInsertDto)) {
            throw new Step4IllegalArgumentException("Unable to create a new 4-tuple because the provided FourTupleInsertDto is null.");
        }

        log.info("Verifying if the analysis exists");
        microStampAuthClient.getAnalysisById(fourTupleInsertDto.getAnalysisId());

        List<UnsafeControlActionReadDto> unsafeControlActionReadDtos = new ArrayList<>();

        log.info("Verifying if the UCAs exists");
        for(UUID ucaId : fourTupleInsertDto.getUnsafeControlActionIds())
            unsafeControlActionReadDtos.add(microStampStep3Client.readUnsafeControlAction(ucaId));

        FourTuple fourTuple = FourTupleMapper.toEntity(fourTupleInsertDto);
        log.info("Inserting the 4-tuple {} on the database", fourTuple);
        fourTupleRepository.save(fourTuple);

        return FourTupleMapper.toFullDto(fourTuple, unsafeControlActionReadDtos);
    }

    public void update(UUID id, FourTupleUpdateDto fourTupleUpdateDto) throws Step4NotFoundException {
        log.debug("Verifying if the 4-tuple update is valid");
        if (Objects.isNull(fourTupleUpdateDto)) {
            throw new Step4IllegalArgumentException("Unable to create a new 4-tuple because the provided FourTupleUpdateDto is null.");
        }

        log.debug("Finding if there is a 4-tuple with id {} to update", id);
        FourTuple fourTuple = fourTupleRepository.findById(id)
                .orElseThrow(() -> new Step4NotFoundException("4-tuple", id.toString()));

        log.info("Verifying if the UCAs exists");
        for(UUID ucaId : fourTupleUpdateDto.getUnsafeControlActionIds())
            microStampStep3Client.readUnsafeControlAction(ucaId);

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

    private List<UnsafeControlActionReadDto> fetchUCAsFromExistingFourTuple(FourTuple fourTuple) {
        log.info("Fetching UCAs from 4-tuple: {}", fourTuple.getId());

        List<UUID> originalUcaIds = new ArrayList<>(fourTuple.getUnsafeControlActions());

        return originalUcaIds.stream()
                .map(ucaId -> {
                    try {
                        return Optional.of(microStampStep3Client.readUnsafeControlAction(ucaId));
                    } catch (FeignException.NotFound ex) {
                        handleUCANotFound(fourTuple, ucaId);
                        return Optional.<UnsafeControlActionReadDto>empty();
                    }
                })
                .flatMap(Optional::stream)
                .sorted(Comparator.comparing(UnsafeControlActionReadDto::getUca_code))
                .toList();
    }

    private void handleUCANotFound(FourTuple fourTuple, UUID ucaId) {
        log.info("UCA {} from 4-tuple {} was deleted, deleting the existing reference on the database", ucaId, fourTuple.getId());
        List<UUID> ucaIds = fourTuple.getUnsafeControlActions();
        ucaIds.remove(ucaId);

        fourTuple.setUnsafeControlActions(ucaIds);
        fourTupleRepository.save(fourTuple);
    }
}
