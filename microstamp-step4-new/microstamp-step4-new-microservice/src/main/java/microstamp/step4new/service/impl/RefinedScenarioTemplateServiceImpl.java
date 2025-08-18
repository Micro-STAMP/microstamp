package microstamp.step4new.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.step3.dto.UnsafeControlActionFullReadDto;
import microstamp.step4new.client.MicroStampStep3Client;
import microstamp.step4new.dto.refinedscenario.RefinedScenarioCommonCauseReadDto;
import microstamp.step4new.dto.refinedscenario.RefinedScenarioTemplateReadDto;
import microstamp.step4new.mapper.RefinedScenarioCommonCauseMapper;
import microstamp.step4new.mapper.RefinedScenarioTemplateMapper;
import microstamp.step4new.repository.RefinedScenarioTemplateRepository;
import microstamp.step4new.service.RefinedScenarioTemplateService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Log4j2
@Component
@AllArgsConstructor
public class RefinedScenarioTemplateServiceImpl implements RefinedScenarioTemplateService {

	private final RefinedScenarioTemplateRepository templateRepository;
    private final MicroStampStep3Client step3Client;

	@Override
	public List<RefinedScenarioTemplateReadDto> findByCommonCauseCode(String code) {
		log.info("Finding templates by common cause code {}", code);
		return templateRepository.findByCommonCause_Code(code).stream()
				.map(RefinedScenarioTemplateMapper::toDto)
				.toList();
	}

	@Override
	public List<RefinedScenarioTemplateReadDto> findAll() {
		log.info("Finding all refined scenario templates");
		return templateRepository.findAll().stream()
				.map(RefinedScenarioTemplateMapper::toDto)
				.toList();
	}

	@Override
	public List<RefinedScenarioTemplateReadDto> applyTemplates(UUID unsafeControlActionId) {
		log.info("Applying templates for UCA {}", unsafeControlActionId);
		UnsafeControlActionFullReadDto uca = step3Client.readUnsafeControlAction(unsafeControlActionId);
		return java.util.stream.Stream.concat(
				templateRepository.findByUnsafeControlActionType(uca.type()).stream(),
				templateRepository.findByUnsafeControlActionTypeIsNull().stream()
		)
				.map(t -> RefinedScenarioTemplateMapper.toAppliedDto(t, uca))
				.toList();
	}

	@Override
	public List<RefinedScenarioTemplateReadDto> applyTemplatesByCommonCause(UUID unsafeControlActionId, String commonCauseCode) {
		log.info("Applying templates for UCA {} filtered by common cause {}", unsafeControlActionId, commonCauseCode);
		UnsafeControlActionFullReadDto uca = step3Client.readUnsafeControlAction(unsafeControlActionId);
		return templateRepository.findByCommonCause_Code(commonCauseCode).stream()
				.filter(t -> t.getUnsafeControlActionType() == null || t.getUnsafeControlActionType().equals(uca.type()))
				.map(t -> RefinedScenarioTemplateMapper.toAppliedDto(t, uca))
				.toList();
	}

	@Override
	public List<RefinedScenarioCommonCauseReadDto> applyTemplatesGroupedByCommonCause(UUID unsafeControlActionId) {
		log.info("Applying templates grouped by common cause for UCA {}", unsafeControlActionId);
		var applied = applyTemplates(unsafeControlActionId);
		var grouped = applied.stream().collect(java.util.stream.Collectors.groupingBy(t -> t.getCommonCause().getCode()));
		return grouped.values().stream()
				.map(RefinedScenarioCommonCauseMapper::toDtoGroupedFromApplied)
				.toList();
	}
}