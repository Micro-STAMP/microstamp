package microstamp.step1.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.step1.client.MicroStampClient;
import microstamp.step1.dto.hazard.HazardInsertDto;
import microstamp.step1.dto.hazard.HazardReadDto;
import microstamp.step1.dto.hazard.HazardUpdateDto;
import microstamp.step1.entity.Hazard;
import microstamp.step1.entity.Loss;
import microstamp.step1.exception.Step1IllegalArgumentException;
import microstamp.step1.exception.Step1NotFoundException;
import microstamp.step1.exception.Step1OrphanException;
import microstamp.step1.exception.Step1SelfParentingHazardException;
import microstamp.step1.mapper.HazardMapper;
import microstamp.step1.repository.HazardRepository;
import microstamp.step1.repository.LossRepository;
import microstamp.step1.service.HazardService;
import org.springframework.stereotype.Component;

import java.util.*;

@Log4j2
@Component
@AllArgsConstructor
public class HazardServiceImpl implements HazardService {

    private final HazardRepository hazardRepository;

    private final LossRepository lossRepository;

    private final MicroStampClient microStampClient;

    public List<HazardReadDto> findAll() {
        log.info("Finding all hazards");
        return hazardRepository.findAll().stream()
                .map(HazardMapper::toDto)
                .sorted(Comparator.comparing(HazardReadDto::getName))
                .toList();
    }

    public HazardReadDto findById(UUID id) throws Step1NotFoundException {
        log.info("Finding hazard by id: {}", id);
        return HazardMapper.toDto(hazardRepository.findById(id)
                .orElseThrow(() -> new Step1NotFoundException("Hazard", id.toString())));
    }

    public List<HazardReadDto> findByAnalysisId(UUID id) {
        log.info("Finding hazard by the analysis id: {}", id);
        return hazardRepository.findByAnalysisId(id).stream()
                .map(HazardMapper::toDto)
                .sorted(Comparator.comparing(HazardReadDto::getName))
                .toList();
    }

    public HazardReadDto insert(HazardInsertDto hazardInsertDto) throws Step1NotFoundException {
        log.debug("Verifying if the hazard insert is valid");
        if (Objects.isNull(hazardInsertDto)) {
            throw new Step1IllegalArgumentException("Unable to create a new hazard because the provided HazardInsertDto is null.");
        }

        log.info("Verifying if the analysis exists on the database");
        microStampClient.getAnalysisById(hazardInsertDto.getAnalysisId());

        Hazard hazard = HazardMapper.toEntity(hazardInsertDto);

        log.debug("Finding losses {} associated with the hazard {}", hazardInsertDto.getLossIds(), hazard.getId());
        List<Loss> lossEntities = Optional.ofNullable(hazardInsertDto.getLossIds())
                .orElseGet(Collections::emptyList)
                .stream()
                .map(id -> lossRepository.findById(id)
                .orElseThrow(() -> new Step1NotFoundException("Loss",  id.toString()))).toList();
        hazard.setLossEntities(lossEntities);

        hazard.setFather(setFatherForInsert(hazardInsertDto, hazard));

        log.debug("Saving the hazard {}", hazard);
        hazardRepository.save(hazard);

        return HazardMapper.toDto(hazard);
    }

    public void update(UUID id, HazardUpdateDto hazardUpdateDto) throws Step1NotFoundException {
        Hazard hazard = hazardRepository.findById(id)
                .orElseThrow(() -> new Step1NotFoundException("Hazard", id.toString()));

        hazard.setName(hazardUpdateDto.getName());
        List<Loss> lossEntities = Optional.ofNullable(hazardUpdateDto.getLossIds())
                .orElseGet(Collections::emptyList)
                .stream()
                .map(lossId -> lossRepository.findById(lossId).orElseThrow(() -> new Step1NotFoundException("Loss", lossId.toString())))
                .toList();
        hazard.setLossEntities(lossEntities);

        hazard.setFather(setFatherForUpdate(id, hazardUpdateDto));

        hazardRepository.save(hazard);
    }

    private Hazard setFatherForUpdate(UUID id, HazardUpdateDto hazardUpdateDto) {
        if (Objects.isNull(hazardUpdateDto.getFatherId())) {
            log.debug("The father from the hazard {} is null", id);
            return null;
        }

        if (hazardUpdateDto.getFatherId().equals(id)) {
            log.error("The hazard and the parent (id {}) are the same.", id);
            throw new Step1SelfParentingHazardException();
        }

        Hazard father = hazardRepository.findById(hazardUpdateDto.getFatherId())
                .orElseThrow(() -> new Step1NotFoundException("Hazard", hazardUpdateDto.getFatherId().toString()));

        List<HazardReadDto> children = getHazardChildren(id);

        if (children.stream().anyMatch(child -> child.getId().equals(hazardUpdateDto.getFatherId()))) {
            log.error("The hazard {} is its own parent.", hazardUpdateDto.getFatherId());
            throw new Step1OrphanException();
        }

        return father;
    }

    public void delete(UUID id) throws Step1NotFoundException {
        log.info("Deleting the hazard {} and its children hazards", id);
        deleteHazardAndChildren(id);
    }

    public List<HazardReadDto> getHazardChildren(UUID id) throws Step1NotFoundException {
        log.debug("Trying to find a hazard with id {}", id);
        Hazard hazard = hazardRepository.findById(id)
                .orElseThrow(() -> new Step1NotFoundException("Hazard", id.toString()));

        List<Hazard> children = new ArrayList<>();
        getChildren(hazard, children);

        return children.stream()
                .map(HazardMapper::toDto)
                .sorted(Comparator.comparing(HazardReadDto::getName))
                .toList();
    }

    private void deleteHazardAndChildren(UUID id) {
        log.debug("Finding the hazard and its children for id {}", id.toString());
        List<Hazard> hazards = hazardRepository.findHazardChildren(id.toString());

        for (Hazard h : hazards)
            deleteHazardAndChildren(h.getId());

        hazardRepository.deleteLossesAssociation(id.toString());
        hazardRepository.deleteSystemSafetyConstraintsAssociation(id.toString());
        hazardRepository.deleteById(id);
    }

    private void getChildren(Hazard parent, List<Hazard> children) {
        log.info("Getting all children for hazard {}", parent.getId());
        List<Hazard> directChildren = hazardRepository.findHazardChildren(parent.getId().toString());

        for (Hazard child : directChildren) {
            children.add(child);
            getChildren(child, children);
        }
    }

    private Hazard setFatherForInsert(HazardInsertDto hazardInsertDto, Hazard hazard) {
        if (Objects.isNull(hazardInsertDto.getFatherId())) {
            log.debug("No father is found. Returning null");
            return null;
        }

        log.info("Trying to fetch the father with id {}", hazardInsertDto.getFatherId());
        return hazardRepository.findById(hazardInsertDto.getFatherId())
                .orElseThrow(() -> new Step1NotFoundException("Hazard", hazardInsertDto.getFatherId().toString()));
    }
}
