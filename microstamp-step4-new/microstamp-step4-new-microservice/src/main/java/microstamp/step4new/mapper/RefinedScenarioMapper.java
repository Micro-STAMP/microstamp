package microstamp.step4new.mapper;

import microstamp.step4new.dto.refinedscenario.*;
import microstamp.step4new.entity.RefinedScenario;
import microstamp.step4new.entity.RefinedScenarioCommonCause;

public class RefinedScenarioMapper {

	public static RefinedScenarioReadDto toDto(RefinedScenario entity) {
		return RefinedScenarioReadDto.builder()
				.id(entity.getId())
				.commonCauseId(entity.getCommonCause().getId())
				.refinedScenario(entity.getRefinedScenario())
				.formalScenarioClassId(entity.getFormalScenarioClass().getId())
				.unsafeControlActionId(entity.getUnsafeControlActionId())
				.build();
	}

	public static void applyUpdate(RefinedScenario entity, RefinedScenarioUpdateDto dto, RefinedScenarioCommonCause commonCause) {
		entity.setCommonCause(commonCause);
		entity.setRefinedScenario(dto.getRefinedScenario());
	}

	public static RefinedScenario toEntity(RefinedScenarioInsertDto dto, RefinedScenarioCommonCause commonCause) {
		return RefinedScenario.builder()
				.commonCause(commonCause)
				.refinedScenario(dto.getRefinedScenario())
				.unsafeControlActionId(dto.getUnsafeControlActionId())
				.build();
	}
}