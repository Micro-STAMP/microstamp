package microstamp.step2.mapper;

import microstamp.step1.dto.systemsafetyconstraint.SystemSafetyConstraintReadDto;
import microstamp.step2.dto.responsibility.ResponsibilityInsertDto;
import microstamp.step2.dto.responsibility.ResponsibilityReadDto;
import microstamp.step2.entity.Component;
import microstamp.step2.entity.Responsibility;

public class ResponsibilityMapper {

    public static ResponsibilityReadDto toDto(Responsibility responsibility){
        return toDto(responsibility, null);
    }

    public static ResponsibilityReadDto toDto(Responsibility responsibility, SystemSafetyConstraintReadDto systemSafetyConstraintReadDto){
        return ResponsibilityReadDto.builder()
                .id(responsibility.getId())
                .responsibility(responsibility.getResponsibility())
                .code(responsibility.getCode())
                .systemSafetyConstraint(systemSafetyConstraintReadDto)
                .build();
    }

    public static Responsibility toEntity(ResponsibilityInsertDto responsibilityInsertDto, Component component){
        return Responsibility.builder()
                .responsibility(responsibilityInsertDto.getResponsibility())
                .code(responsibilityInsertDto.getCode())
                .systemSafetyConstraintId(responsibilityInsertDto.getSystemSafetyConstraintId())
                .component(component)
                .build();
    }
}
