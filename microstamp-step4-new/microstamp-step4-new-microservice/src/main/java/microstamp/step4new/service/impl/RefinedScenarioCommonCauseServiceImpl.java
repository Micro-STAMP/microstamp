package microstamp.step4new.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.step4new.dto.refinedscenario.RefinedScenarioCommonCauseReadDto;
import microstamp.step4new.entity.RefinedScenarioCommonCause;
import microstamp.step4new.exception.Step4NewNotFoundException;
import microstamp.step4new.mapper.RefinedScenarioCommonCauseMapper;
import microstamp.step4new.repository.RefinedScenarioCommonCauseRepository;
import microstamp.step4new.repository.RefinedScenarioTemplateRepository;
import microstamp.step4new.service.RefinedScenarioCommonCauseService;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Log4j2
@Component
@AllArgsConstructor
public class RefinedScenarioCommonCauseServiceImpl implements RefinedScenarioCommonCauseService {

	private final RefinedScenarioCommonCauseRepository commonCauseRepository;
	private final RefinedScenarioTemplateRepository templateRepository;

	@Override
	public List<RefinedScenarioCommonCauseReadDto> findAll() {
		log.info("Finding all RefinedScenario common causes");
		return commonCauseRepository.findAll().stream()
				.sorted(Comparator.comparing(RefinedScenarioCommonCause::getCode))
				.map(cc -> {
					RefinedScenarioCommonCauseReadDto dto = RefinedScenarioCommonCauseMapper.toDto(cc);
					dto.setTemplates(templateRepository.findByCommonCause_Id(cc.getId()).stream()
							.map(t -> new microstamp.step4new.dto.refinedscenario.RefinedScenarioTemplateSimpleReadDto(
									t.getId(), t.getTemplate(), t.getUnsafeControlActionType() == null ? null : t.getUnsafeControlActionType().name()))
							.toList());
					return dto;
				})
				.toList();
	}

	@Override
	public RefinedScenarioCommonCauseReadDto findByCode(String code) {
		log.info("Finding RefinedScenario common cause by code {}", code);
		var cc = commonCauseRepository.findByCode(code)
				.orElseThrow(() -> new Step4NewNotFoundException("RefinedScenarioCommonCause", code));
		var dto = RefinedScenarioCommonCauseMapper.toDto(cc);
		dto.setTemplates(templateRepository.findByCommonCause_Id(cc.getId()).stream()
				.map(t -> new microstamp.step4new.dto.refinedscenario.RefinedScenarioTemplateSimpleReadDto(
						t.getId(), t.getTemplate(), t.getUnsafeControlActionType() == null ? null : t.getUnsafeControlActionType().name()))
				.toList());
		return dto;
	}
}