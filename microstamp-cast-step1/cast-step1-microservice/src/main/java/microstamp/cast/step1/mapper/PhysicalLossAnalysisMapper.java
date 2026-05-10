package microstamp.cast.step1.mapper;

import microstamp.cast.step1.dto.physicallossanalysis.PhysicalLossAnalysisInsertDto;
import microstamp.cast.step1.dto.physicallossanalysis.PhysicalLossAnalysisReadDto;
import microstamp.cast.step1.entity.PhysicalLossAnalysis;

public class PhysicalLossAnalysisMapper {

    public static PhysicalLossAnalysisReadDto toDto(PhysicalLossAnalysis entity) {
        return PhysicalLossAnalysisReadDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .physicalLossDescription(entity.getPhysicalLossDescription())
                .affectedEquipment(entity.getAffectedEquipment())
                .physicalDesignRequirements(entity.getPhysicalDesignRequirements())
                .physicalControls(entity.getPhysicalControls())
                .failuresAndUnsafeInteractions(entity.getFailuresAndUnsafeInteractions())
                .missingOrInadequateControls(entity.getMissingOrInadequateControls())
                .contextualFactors(entity.getContextualFactors())
                .build();
    }

    public static PhysicalLossAnalysis toEntity(PhysicalLossAnalysisInsertDto dto) {
        return PhysicalLossAnalysis.builder()
                .code(dto.getCode())
                .physicalLossDescription(dto.getPhysicalLossDescription())
                .affectedEquipment(dto.getAffectedEquipment())
                .physicalDesignRequirements(dto.getPhysicalDesignRequirements())
                .physicalControls(dto.getPhysicalControls())
                .failuresAndUnsafeInteractions(dto.getFailuresAndUnsafeInteractions())
                .missingOrInadequateControls(dto.getMissingOrInadequateControls())
                .contextualFactors(dto.getContextualFactors())
                .analysisId(dto.getAnalysisId())
                .build();
    }
}