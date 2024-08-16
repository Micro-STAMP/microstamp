package microstamp.step1.mapper;

import microstamp.step1.dto.hazard.HazardReadDto;
import microstamp.step1.dto.systemsafetyconstraint.SystemSafetyConstraintInsertDto;
import microstamp.step1.dto.systemsafetyconstraint.SystemSafetyConstraintReadDto;
import microstamp.step1.entity.SystemSafetyConstraint;

import java.util.Comparator;

public class SystemSafetyConstraintMapper {

    public static SystemSafetyConstraintReadDto toDto(SystemSafetyConstraint systemSafetyConstraint){
        return SystemSafetyConstraintReadDto.builder()
                .id(systemSafetyConstraint.getId())
                .name(systemSafetyConstraint.getName())
                .code(systemSafetyConstraint.getCode())
                .hazards(systemSafetyConstraint.getHazardEntities() != null
                        ? systemSafetyConstraint.getHazardEntities().stream()
                        .map(HazardMapper::toDto)
                        .sorted(Comparator.comparing(HazardReadDto::getName))
                        .toList()
                        : null)
                .build();
    }

    public static SystemSafetyConstraint toEntity(SystemSafetyConstraintInsertDto systemSafetyConstraintInsertDto){
        return SystemSafetyConstraint.builder()
                .name(systemSafetyConstraintInsertDto.getName())
                .code(systemSafetyConstraintInsertDto.getCode())
                .analysisId(systemSafetyConstraintInsertDto.getAnalysisId())
                .build();
    }
}
