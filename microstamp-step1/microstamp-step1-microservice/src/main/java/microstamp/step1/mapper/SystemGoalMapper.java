package microstamp.step1.mapper;

import microstamp.step1.dto.systemgoal.SystemGoalInsertDto;
import microstamp.step1.dto.systemgoal.SystemGoalReadDto;
import microstamp.step1.entity.SystemGoal;

public class SystemGoalMapper {

    public static SystemGoalReadDto toDto(SystemGoal systemGoal){
        return SystemGoalReadDto.builder()
                .id(systemGoal.getId())
                .name(systemGoal.getName())
                .code(systemGoal.getCode())
                .build();
    }

    public static SystemGoal toEntity(SystemGoalInsertDto systemGoalInsertDto){
        return SystemGoal.builder()
                .name(systemGoalInsertDto.getName())
                .code(systemGoalInsertDto.getCode())
                .analysisId(systemGoalInsertDto.getAnalysisId())
                .build();
    }
}
