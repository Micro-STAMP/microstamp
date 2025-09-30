package microstamp.step4new.mapper;

import microstamp.step3.dto.UnsafeControlActionFullReadDto;
import microstamp.step4new.dto.refinedscenario.RefinedScenarioTemplateReadDto;
import microstamp.step4new.entity.RefinedScenarioTemplate;
import microstamp.step4new.helper.RefinedScenarioHelper;
import microstamp.step4new.dto.refinedscenario.RefinedScenarioCommonCauseSimpleReadDto;

public class RefinedScenarioTemplateMapper {

	public static RefinedScenarioTemplateReadDto toDto(RefinedScenarioTemplate entity) {
		return RefinedScenarioTemplateReadDto.builder()
				.id(entity.getId())
				.template(entity.getTemplate())
				.unsafeControlActionType(entity.getUnsafeControlActionType() == null ? null : entity.getUnsafeControlActionType().name())
				.commonCause(RefinedScenarioCommonCauseSimpleReadDto.builder()
						.id(entity.getCommonCause().getId())
						.code(entity.getCommonCause().getCode())
						.cause(entity.getCommonCause().getCommonCause())
						.build())
				.build();
	}

	public static RefinedScenarioTemplateReadDto toAppliedDto(RefinedScenarioTemplate template, UnsafeControlActionFullReadDto uca) {
		return RefinedScenarioTemplateReadDto.builder()
				.id(template.getId())
				.template(RefinedScenarioHelper.applyTemplate(template.getTemplate(), uca))
				.unsafeControlActionType(template.getUnsafeControlActionType() == null ? null : template.getUnsafeControlActionType().name())
				.commonCause(RefinedScenarioCommonCauseSimpleReadDto.builder()
						.id(template.getCommonCause().getId())
						.code(template.getCommonCause().getCode())
						.cause(template.getCommonCause().getCommonCause())
						.build())
				.build();
	}
}