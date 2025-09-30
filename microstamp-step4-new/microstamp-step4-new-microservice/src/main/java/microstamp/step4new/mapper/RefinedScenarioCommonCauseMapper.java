package microstamp.step4new.mapper;

import microstamp.step4new.dto.refinedscenario.RefinedScenarioCommonCauseReadDto;
import microstamp.step4new.dto.refinedscenario.RefinedScenarioCommonCauseGroupReadDto;
import microstamp.step4new.dto.refinedscenario.RefinedScenarioReadDto;
import microstamp.step4new.dto.refinedscenario.RefinedScenarioTemplateReadDto;
import microstamp.step4new.dto.refinedscenario.RefinedScenarioTemplateSimpleReadDto;
import microstamp.step4new.entity.RefinedScenarioCommonCause;

import java.util.List;

public class RefinedScenarioCommonCauseMapper {

	public static RefinedScenarioCommonCauseReadDto toDto(RefinedScenarioCommonCause entity) {
		return RefinedScenarioCommonCauseReadDto.builder()
				.id(entity.getId())
				.code(entity.getCode())
				.cause(entity.getCommonCause())
				.build();
	}

	public static RefinedScenarioCommonCauseReadDto toDtoGroupedFromApplied(List<RefinedScenarioTemplateReadDto> templatesForCommonCause) {
		var first = templatesForCommonCause.getFirst();
		var ccSimple = first.getCommonCause();
		RefinedScenarioCommonCauseReadDto dto = RefinedScenarioCommonCauseReadDto.builder()
				.id(ccSimple.getId())
				.code(ccSimple.getCode())
				.cause(ccSimple.getCause())
				.build();
		dto.setTemplates(templatesForCommonCause.stream()
				.map(t -> new RefinedScenarioTemplateSimpleReadDto(t.getId(), t.getTemplate(), t.getUnsafeControlActionType()))
				.toList());
		return dto;
	}

	public static RefinedScenarioCommonCauseGroupReadDto toGroupDto(
			RefinedScenarioCommonCause commonCause,
			List<RefinedScenarioTemplateSimpleReadDto> templates,
			List<RefinedScenarioReadDto> refinedScenarios) {
		RefinedScenarioCommonCauseGroupReadDto dto = RefinedScenarioCommonCauseGroupReadDto.builder()
				.id(commonCause.getId())
				.code(commonCause.getCode())
				.cause(commonCause.getCommonCause())
				.build();
		dto.setTemplates(templates);
		dto.setRefinedScenarios(refinedScenarios);
		return dto;
	}
}