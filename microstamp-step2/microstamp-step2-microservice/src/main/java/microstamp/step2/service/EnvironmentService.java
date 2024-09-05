package microstamp.step2.service;

import microstamp.step2.dto.environment.EnvironmentReadDto;

import java.util.UUID;

public interface EnvironmentService {

    EnvironmentReadDto findById(UUID id);

    EnvironmentReadDto findByAnalysisId(UUID id);

    EnvironmentReadDto insert(UUID analysisId);

    void delete(UUID id);
}
