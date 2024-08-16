package microstamp.step2.service;

import microstamp.step2.dto.controlstructure.ControlStructureReadDto;

import java.util.UUID;

public interface ControlStructureService {

    ControlStructureReadDto findByAnalysisId(UUID id);

}
