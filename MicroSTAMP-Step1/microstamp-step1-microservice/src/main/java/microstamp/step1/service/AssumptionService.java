package microstamp.step1.service;

import microstamp.step1.dto.assumption.AssumptionInsertDto;
import microstamp.step1.dto.assumption.AssumptionReadDto;
import microstamp.step1.dto.assumption.AssumptionUpdateDto;

import java.util.List;
import java.util.UUID;

public interface AssumptionService {

    /**
     * Find all assumptions
     *
     * @return List<AssumptionReadDto> containing all assumptions in the database
     */
    List<AssumptionReadDto> findAll();

    /**
     * Find an assumption by it UUID
     *
     * @param id The UUID from assumption
     * @return AssumptionReadDto containing the information of the assumption
     */
    AssumptionReadDto findById(UUID id);

    /**
     * Find all assumptions for an analysis
     * @param id The id of the analysis
     * @return List<AssumptionReadDto> containing all assumptions found for the provided analysis id
     */
    List<AssumptionReadDto> findByAnalysisId(UUID id);

    /**
     * Create a new record of the analysis
     *
     * @param assumptionInsertDto The information of the new assumption
     * @return AssumptionReadDto containing the information of the created assumption
     */
    AssumptionReadDto insert(AssumptionInsertDto assumptionInsertDto);

    /**
     * Updates the name of a provided assumption
     *
     * @param id The id of the assumption
     * @param assumptionUpdateDto The information containing the values to be modified for the given assumption
     */
    void update(UUID id, AssumptionUpdateDto assumptionUpdateDto);

    /**
     * Delete a given assumption by id.
     *
     * @param id The id of the assumption
     */
    void delete(UUID id);

}
