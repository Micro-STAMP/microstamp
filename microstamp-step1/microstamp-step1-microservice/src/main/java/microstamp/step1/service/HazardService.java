package microstamp.step1.service;

import microstamp.step1.dto.hazard.HazardInsertDto;
import microstamp.step1.dto.hazard.HazardReadDto;
import microstamp.step1.dto.hazard.HazardUpdateDto;

import java.util.List;
import java.util.UUID;

public interface HazardService {

    /**
     * Find all hazards
     *
     * @return List<HazardReadDto> containing all hazards in the database
     */
    List<HazardReadDto> findAll();

    /**
     * Find a hazard by it UUID
     *
     * @param id The UUID from hazard
     * @return HazardReadDto containing the information of the hazard
     */
    HazardReadDto findById(UUID id);

    /**
     * Find all hazards from an analysis
     * @param id The id of the analysis
     * @return List<HazardReadDto> containing all hazards found for the provided analysis id
     */
    List<HazardReadDto> findByAnalysisId(UUID id);

    /**
     * Create a new record of the analysis
     *
     * @param hazardInsertDto The information of the new hazard
     * @return HazardReadDto containing the information of the created hazard
     */
    HazardReadDto insert(HazardInsertDto hazardInsertDto);

    /**
     * Updates the name of a provided hazard
     *
     * @param id The id of the hazard
     * @param hazardUpdateDto The information containing the values to be modified for the given hazard
     */
    void update(UUID id, HazardUpdateDto hazardUpdateDto);

    /**
     * Delete a given hazard by id.
     *
     * @param id The id of the hazard
     */
    void delete(UUID id);

    /**
     * Get all the children of a given hazard
     *
     * @param id The id of the hazard
     * @return List<HazardReadDto> containing all children hazards
     */
    List<HazardReadDto> getHazardChildren(UUID id);

}
