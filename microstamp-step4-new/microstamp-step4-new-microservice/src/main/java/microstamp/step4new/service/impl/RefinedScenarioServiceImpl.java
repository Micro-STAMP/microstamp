package microstamp.step4new.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.step4new.dto.refinedscenario.*;
import microstamp.step4new.entity.RefinedScenario;
import microstamp.step4new.entity.RefinedScenarioCommonCause;
import microstamp.step4new.exception.Step4NewNotFoundException;
import microstamp.step4new.mapper.RefinedScenarioMapper;
import microstamp.step4new.mapper.RefinedScenarioCommonCauseMapper;
import microstamp.step4new.repository.RefinedScenarioCommonCauseRepository;
import microstamp.step4new.repository.RefinedScenarioRepository;
import microstamp.step4new.repository.RefinedScenarioTemplateRepository;
import microstamp.step4new.service.RefinedScenarioService;
import microstamp.step4new.repository.FormalScenarioClassRepository;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@Component
@AllArgsConstructor
public class RefinedScenarioServiceImpl implements RefinedScenarioService {

	private final RefinedScenarioCommonCauseRepository commonCauseRepository;
	private final RefinedScenarioRepository refinedScenarioRepository;
	private final RefinedScenarioTemplateRepository templateRepository;
    private final FormalScenarioClassRepository formalScenarioClassRepository;


	@Override
	public List<RefinedScenarioReadDto> findByUnsafeControlActionId(UUID unsafeControlActionId) {
		log.info("Finding RefinedScenarios by UCA {}", unsafeControlActionId);
		return refinedScenarioRepository.findByUnsafeControlActionId(unsafeControlActionId).stream()
				.map(RefinedScenarioMapper::toDto)
				.toList();
	}

	@Override
	public List<RefinedScenarioCommonCauseGroupReadDto> groupByCommonCauseForUnsafeControlAction(UUID unsafeControlActionId) {
		log.info("Grouping RefinedScenarios by common cause for UCA {}", unsafeControlActionId);
		Map<RefinedScenarioCommonCause, List<RefinedScenario>> grouped = refinedScenarioRepository.findByUnsafeControlActionId(unsafeControlActionId)
				.stream().collect(Collectors.groupingBy(RefinedScenario::getCommonCause));
		return grouped.entrySet().stream()
				.sorted(Map.Entry.comparingByKey(Comparator.comparing(RefinedScenarioCommonCause::getCode)))
				.map(entry -> {
					RefinedScenarioCommonCause cc = entry.getKey();
					List<RefinedScenarioReadDto> refinedDtos = entry.getValue().stream().map(RefinedScenarioMapper::toDto).toList();
					List<RefinedScenarioTemplateSimpleReadDto> templateDtos = templateRepository.findByCommonCause_Id(cc.getId()).stream()
							.map(t -> new RefinedScenarioTemplateSimpleReadDto(t.getId(), t.getTemplate(), t.getUnsafeControlActionType() == null ? null : t.getUnsafeControlActionType().name()))
							.toList();
					return RefinedScenarioCommonCauseMapper.toGroupDto(cc, templateDtos, refinedDtos);
				})
				.toList();
	}

	@Override
	public RefinedScenarioReadDto insert(RefinedScenarioInsertDto dto) {
		log.info("Inserting RefinedScenario for UCA {}", dto.getUnsafeControlActionId());
		RefinedScenarioCommonCause cc = commonCauseRepository.findById(dto.getCommonCauseId())
				.orElseThrow(() -> new Step4NewNotFoundException("RefinedScenarioCommonCause", dto.getCommonCauseId().toString()));
		var formalClass = formalScenarioClassRepository.findById(dto.getFormalScenarioClassId())
				.orElseThrow(() -> new Step4NewNotFoundException("FormalScenarioClass", dto.getFormalScenarioClassId().toString()));
		RefinedScenario entity = RefinedScenarioMapper.toEntity(dto, cc);
		entity.setFormalScenarioClass(formalClass);
		RefinedScenario saved = refinedScenarioRepository.save(entity);
		return RefinedScenarioMapper.toDto(saved);
	}

	@Override
	public void update(UUID id, RefinedScenarioUpdateDto dto) {
		log.info("Updating RefinedScenario {}", id);
		RefinedScenario entity = refinedScenarioRepository.findById(id)
				.orElseThrow(() -> new Step4NewNotFoundException("RefinedScenario", id.toString()));
		RefinedScenarioCommonCause cc = commonCauseRepository.findById(dto.getCommonCauseId())
				.orElseThrow(() -> new Step4NewNotFoundException("RefinedScenarioCommonCause", dto.getCommonCauseId().toString()));
		RefinedScenarioMapper.applyUpdate(entity, dto, cc);
		refinedScenarioRepository.save(entity);
	}

	@Override
	public void delete(UUID id) {
		log.info("Deleting RefinedScenario {}", id);
		RefinedScenario entity = refinedScenarioRepository.findById(id)
				.orElseThrow(() -> new Step4NewNotFoundException("RefinedScenario", id.toString()));
		refinedScenarioRepository.delete(entity);
	}
}