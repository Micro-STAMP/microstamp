package microstamp.step1.service;

import microstamp.step1.dto.systemsafetyconstraint.SystemSafetyConstraintInsertDto;
import microstamp.step1.dto.systemsafetyconstraint.SystemSafetyConstraintReadDto;
import microstamp.step1.dto.systemsafetyconstraint.SystemSafetyConstraintUpdateDto;

import java.util.List;
import java.util.UUID;

public interface SystemSafetyConstraintService {

    /**
     * Find all system safety constraints
     *
     * @return List<SystemSafetyConstraintReadDto> containing all system safety constraints in the database
     */
    List<SystemSafetyConstraintReadDto> findAll();

    /**
     * Find a system safety constraint by it UUID
     *
     * @param id The UUID from assumption
     * @return SystemSafetyConstraintReadDto containing the information of the system safety constraint
     */
    SystemSafetyConstraintReadDto findById(UUID id);

    /**
     * Find all system safety constraints for an analysis
     * @param id The id of the analysis
     * @return List<SystemSafetyConstraintReadDto> containing all system safety constraints found for the provided analysis id
     */
    List<SystemSafetyConstraintReadDto> findByAnalysisId(UUID id);

    /**
     * Create a new record of the analysis
     *
     * @param systemSafetyConstraintInsertDto The information of the new system safety constraint
     * @return SystemSafetyConstraintReadDto containing the information of the created system safety constraint
     */
    SystemSafetyConstraintReadDto insert(SystemSafetyConstraintInsertDto systemSafetyConstraintInsertDto);

    /**
     * Updates the name of a provided system safety constraint
     *
     * @param id The id of the system safety constraint
     * @param systemSafetyConstraintUpdateDto The information containing the values to be modified for the given system safety constraint
     */
    void update(UUID id, SystemSafetyConstraintUpdateDto systemSafetyConstraintUpdateDto);

    /**
     * Delete a given system safety constraint by id.
     *
     * @param id The id of the system safety constraint
     */
    void delete(UUID id);
}
