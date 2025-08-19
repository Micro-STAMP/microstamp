package microstamp.step4new.mapper;

import microstamp.step4new.dto.mitigation.MitigationInsertDto;
import microstamp.step4new.dto.mitigation.MitigationReadDto;
import microstamp.step4new.dto.mitigation.MitigationUpdateDto;
import microstamp.step4new.entity.Mitigation;
import microstamp.step4new.entity.RefinedScenario;

public class MitigationMapper {

	public static Mitigation toEntity(MitigationInsertDto dto, RefinedScenario refinedScenario) {
		return Mitigation.builder()
				.refinedScenario(refinedScenario)
				.mitigation(dto.getMitigation())
				.build();
	}

	public static void applyUpdate(Mitigation entity, MitigationUpdateDto dto) {
		entity.setMitigation(dto.getMitigation());
	}

	public static MitigationReadDto toDto(Mitigation entity) {
		return MitigationReadDto.builder()
				.id(entity.getId())
				.refinedScenarioId(entity.getRefinedScenario().getId())
				.mitigation(entity.getMitigation())
				.build();
	}
}


