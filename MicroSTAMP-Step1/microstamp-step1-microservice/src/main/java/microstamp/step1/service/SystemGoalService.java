package microstamp.step1.service;

import microstamp.step1.dto.systemgoal.SystemGoalInsertDto;
import microstamp.step1.dto.systemgoal.SystemGoalReadDto;
import microstamp.step1.dto.systemgoal.SystemGoalUpdateDto;

import java.util.List;
import java.util.UUID;

public interface SystemGoalService {

    /**
     * Find all system goals
     *
     * @return List<SystemGoalReadDto> containing all system goals in the database
     */
    List<SystemGoalReadDto> findAll();

    /**
     * Find a system goal by it UUID
     *
     * @param id The UUID from system goal
     * @return SystemGoalReadDto containing the information of the system goal
     */
    SystemGoalReadDto findById(UUID id);

    /**
     * Find all system goals for an analysis
     * @param id The id of the analysis
     * @return List<SystemGoalReadDto> containing all system goals found for the provided analysis id
     */
    List<SystemGoalReadDto> findByAnalysisId(UUID id);

    /**
     * Create a new record of the analysis
     *
     * @param systemGoalInsertDto The information of the new system goal
     * @return SystemGoalReadDto containing the information of the created system goal
     */
    SystemGoalReadDto insert(SystemGoalInsertDto systemGoalInsertDto);

    /**
     * Updates the name of a provided system goal
     *
     * @param id The id of the system goal
     * @param systemGoalUpdateDto The information containing the values to be modified for the given system goal
     */
    void update(UUID id, SystemGoalUpdateDto systemGoalUpdateDto);

    /**
     * Delete a given system goal by id.
     *
     * @param id The id of the loss
     */
    void delete(UUID id);

}
