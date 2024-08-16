package microstamp.step1.mapper;

import microstamp.step1.entity.Hazard;
import microstamp.step1.dto.hazard.HazardInsertDto;
import microstamp.step1.dto.hazard.HazardReadDto;
import microstamp.step1.dto.loss.LossReadDto;

import java.util.Comparator;

public class HazardMapper {

    public static HazardReadDto toDto(Hazard hazard){
        return new HazardReadDto(hazard.getId(),
                hazard.getName(),
                hazard.getCode(),
                hazard.getLossEntities() != null
                    ? hazard.getLossEntities().stream()
                        .map(LossMapper::toDto)
                        .sorted(Comparator.comparing(LossReadDto::getName))
                        .toList()
                    : null,
                hazard.getFather() != null
                        ? HazardMapper.toDto(hazard.getFather())
                        : null);
    }

    public static Hazard toEntity(HazardInsertDto hazardInsertDto){
        return new Hazard(hazardInsertDto.getName(),
                hazardInsertDto.getCode(),
                hazardInsertDto.getAnalysisId());
    }
}
