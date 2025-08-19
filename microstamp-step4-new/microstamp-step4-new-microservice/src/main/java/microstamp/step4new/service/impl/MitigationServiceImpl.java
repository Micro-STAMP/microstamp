package microstamp.step4new.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.step4new.dto.mitigation.MitigationInsertDto;
import microstamp.step4new.dto.mitigation.MitigationReadDto;
import microstamp.step4new.dto.mitigation.MitigationUpdateDto;
import microstamp.step4new.entity.Mitigation;
import microstamp.step4new.entity.RefinedScenario;
import microstamp.step4new.exception.Step4NewNotFoundException;
import microstamp.step4new.mapper.MitigationMapper;
import microstamp.step4new.repository.MitigationRepository;
import microstamp.step4new.repository.RefinedScenarioRepository;
import microstamp.step4new.service.MitigationService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Log4j2
@Component
@AllArgsConstructor
public class MitigationServiceImpl implements MitigationService {

	private final MitigationRepository mitigationRepository;
	private final RefinedScenarioRepository refinedScenarioRepository;

	@Override
	public MitigationReadDto findById(UUID id) {
		log.info("Finding Mitigation {}", id);
		return mitigationRepository.findById(id)
				.map(MitigationMapper::toDto)
				.orElseThrow(() -> new Step4NewNotFoundException("Mitigation", id.toString()));
	}

	@Override
	public MitigationReadDto getOrCreateByRefinedScenarioId(UUID refinedScenarioId) {
		log.info("Getting or creating Mitigation for RefinedScenario {}", refinedScenarioId);
		return mitigationRepository.findByRefinedScenarioId(refinedScenarioId)
				.map(MitigationMapper::toDto)
				.orElseGet(() -> create(MitigationInsertDto.builder().refinedScenarioId(refinedScenarioId).mitigation("").build()));
	}

	@Override
	public List<MitigationReadDto> findByUnsafeControlActionId(UUID unsafeControlActionId) {
		log.info("Finding Mitigations by UCA {}", unsafeControlActionId);
		return mitigationRepository.findByRefinedScenario_UnsafeControlActionId(unsafeControlActionId).stream()
				.map(MitigationMapper::toDto)
				.toList();
	}

	@Override
	public MitigationReadDto create(MitigationInsertDto dto) {
		log.info("Creating Mitigation for RefinedScenario {}", dto.getRefinedScenarioId());
		RefinedScenario refinedScenario = refinedScenarioRepository.findById(dto.getRefinedScenarioId())
				.orElseThrow(() -> new Step4NewNotFoundException("RefinedScenario", dto.getRefinedScenarioId().toString()));
		mitigationRepository.findByRefinedScenarioId(dto.getRefinedScenarioId())
				.ifPresent(existing -> { throw new microstamp.step4new.exception.Step4NewIllegalArgumentException("Mitigation already exists for RefinedScenario: " + dto.getRefinedScenarioId()); });
		Mitigation entity = MitigationMapper.toEntity(dto, refinedScenario);
		Mitigation saved = mitigationRepository.save(entity);
		return MitigationMapper.toDto(saved);
	}

	@Override
	public void update(UUID id, MitigationUpdateDto dto) {
		log.info("Updating Mitigation {}", id);
		Mitigation entity = mitigationRepository.findById(id)
				.orElseThrow(() -> new Step4NewNotFoundException("Mitigation", id.toString()));
		MitigationMapper.applyUpdate(entity, dto);
		mitigationRepository.save(entity);
	}

	@Override
	public void delete(UUID id) {
		log.info("Deleting Mitigation {}", id);
		Mitigation entity = mitigationRepository.findById(id)
				.orElseThrow(() -> new Step4NewNotFoundException("Mitigation", id.toString()));
		mitigationRepository.delete(entity);
	}
}


