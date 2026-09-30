package microstamp.step2.mapper;

import microstamp.cast.step1.dto.violatedsystemsafetyconstraint.ViolatedSystemSafetyConstraintReadDto;
import microstamp.step1.dto.systemsafetyconstraint.SystemSafetyConstraintReadDto;
import microstamp.step2.dto.responsibility.ResponsibilityInsertDto;
import microstamp.step2.dto.responsibility.ResponsibilityReadDto;
import microstamp.step2.entity.Component;
import microstamp.step2.entity.Responsibility;

import java.util.List;

public class ResponsibilityMapper {

    public static ResponsibilityReadDto toDto(Responsibility responsibility) {
        return toDto(responsibility, null, List.of());
    }

    public static ResponsibilityReadDto toDto(
            Responsibility responsibility,
            SystemSafetyConstraintReadDto systemSafetyConstraintReadDto,
            List<ViolatedSystemSafetyConstraintReadDto> violatedSystemSafetyConstraintReadDtos
    ) {
        return ResponsibilityReadDto.builder()
                .id(responsibility.getId())
                .responsibility(responsibility.getResponsibility())
                .code(responsibility.getCode())
                .systemSafetyConstraint(systemSafetyConstraintReadDto)
                .violatedSystemSafetyConstraints(violatedSystemSafetyConstraintReadDtos)
                .build();
    }

    public static Responsibility toEntity(ResponsibilityInsertDto responsibilityInsertDto, Component component) {
        return Responsibility.builder()
                .responsibility(responsibilityInsertDto.getResponsibility())
                .code(responsibilityInsertDto.getCode())
                .systemSafetyConstraintId(responsibilityInsertDto.getSystemSafetyConstraintId())
                .violatedSystemSafetyConstraintIds(responsibilityInsertDto.getViolatedSystemSafetyConstraintIds())
                .component(component)
                .build();
    }
}
