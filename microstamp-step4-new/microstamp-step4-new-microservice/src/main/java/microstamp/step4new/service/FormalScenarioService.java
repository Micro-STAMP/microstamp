package microstamp.step4new.service;

import microstamp.step4new.dto.formalscenario.FormalScenarioReadDto;

import java.util.UUID;

public interface FormalScenarioService {

    /**
     * Create a new Formal Scenario for a given analysis and UCA.
     *
     * @param analysisId The UUID of the analysis
     * @param unsafeControlActionId The UUID of the unsafe control action
     * @return FormalScenarioReadDto containing the computed scenario
     */
    FormalScenarioReadDto create(UUID analysisId, UUID unsafeControlActionId);

    /**
     * Find or create a Formal Scenario by UCA id.
     *
     * @param unsafeControlActionId The UUID of the unsafe control action
     * @return FormalScenarioReadDto containing the computed scenario
     */
    FormalScenarioReadDto getOrCreate(UUID unsafeControlActionId);

    /**
     * Find a Formal Scenario by its id.
     *
     * @param id The UUID of the Formal Scenario
     * @return FormalScenarioReadDto containing the computed scenario
     */
    FormalScenarioReadDto findById(UUID id);

    /**
     * Delete a Formal Scenario by id.
     *
     * @param id The UUID of the Formal Scenario
     */
    void delete(UUID id);
}
