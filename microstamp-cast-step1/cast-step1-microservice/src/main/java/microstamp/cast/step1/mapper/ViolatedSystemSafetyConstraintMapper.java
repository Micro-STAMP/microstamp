package microstamp.cast.step1.mapper;

import microstamp.cast.step1.dto.violatedsystemsafetyconstraint.ViolatedSystemSafetyConstraintInsertDto;
import microstamp.cast.step1.dto.violatedsystemsafetyconstraint.ViolatedSystemSafetyConstraintReadDto;
import microstamp.cast.step1.entity.ViolatedSystemSafetyConstraint;

import java.util.Comparator;

public class ViolatedSystemSafetyConstraintMapper {

    public static ViolatedSystemSafetyConstraintReadDto toDto(ViolatedSystemSafetyConstraint entity) {
        return ViolatedSystemSafetyConstraintReadDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .hazards(entity.getHazards() != null
                        ? entity.getHazards().stream()
                        .map(CastHazardMapper::toDto)
                        .sorted(Comparator.comparing(hazard -> hazard.getCode()))
                        .toList()
                        : null)
                .build();
    }

    public static ViolatedSystemSafetyConstraint toEntity(ViolatedSystemSafetyConstraintInsertDto dto) {
        return ViolatedSystemSafetyConstraint.builder()
                .code(dto.getCode())
                .name(dto.getName())
                .description(dto.getDescription())
                .analysisId(dto.getAnalysisId())
                .build();
    }
}