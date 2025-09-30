package microstamp.step4new.mapper;

import microstamp.step4new.dto.highlevelsolution.HighLevelSolutionInsertDto;
import microstamp.step4new.dto.highlevelsolution.HighLevelSolutionReadDto;
import microstamp.step4new.dto.highlevelsolution.HighLevelSolutionUpdateDto;
import microstamp.step4new.entity.FormalScenarioClass;
import microstamp.step4new.entity.HighLevelSolution;

import java.util.UUID;

public class HighLevelSolutionMapper {

    public static HighLevelSolution toEntity(HighLevelSolutionInsertDto dto, FormalScenarioClass formalScenarioClass) {
        return HighLevelSolution.builder()
                .formalScenarioClass(formalScenarioClass)
                .processBehavior(dto.getProcessBehavior())
                .controllerBehavior(dto.getControllerBehavior())
                .otherSolutions(dto.getOtherSolutions())
                .build();
    }

    public static void applyUpdate(HighLevelSolution entity, HighLevelSolutionUpdateDto dto) {
        entity.setProcessBehavior(dto.getProcessBehavior());
        entity.setControllerBehavior(dto.getControllerBehavior());
        entity.setOtherSolutions(dto.getOtherSolutions());
    }

    public static HighLevelSolutionReadDto toDto(HighLevelSolution entity) {
        return HighLevelSolutionReadDto.builder()
                .id(entity.getId())
                .formalScenarioClassId(entity.getFormalScenarioClass().getId())
                .processBehavior(entity.getProcessBehavior())
                .controllerBehavior(entity.getControllerBehavior())
                .otherSolutions(entity.getOtherSolutions())
                .build();
    }

	public static HighLevelSolutionInsertDto toEmptyInsertDto(UUID formalScenarioClassId) {
		return HighLevelSolutionInsertDto.builder()
				.formalScenarioClassId(formalScenarioClassId)
				.processBehavior("")
				.controllerBehavior("")
				.otherSolutions("")
				.build();
	}
}
