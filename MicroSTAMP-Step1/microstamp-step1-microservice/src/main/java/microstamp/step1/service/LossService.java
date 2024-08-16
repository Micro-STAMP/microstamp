package microstamp.step1.service;

import microstamp.step1.dto.loss.LossInsertDto;
import microstamp.step1.dto.loss.LossReadDto;
import microstamp.step1.dto.loss.LossUpdateDto;

import java.util.List;
import java.util.UUID;

public interface LossService {

    /**
     * Find all losses
     *
     * @return List<LossReadDto> containing all losses in the database
     */
    List<LossReadDto> findAll();

    /**
     * Find a loss by it UUID
     *
     * @param id The UUID from loss
     * @return LossReadDto containing the information of the loss
     */
    LossReadDto findById(UUID id);

    /**
     * Find all losses for an analysis
     * @param id The id of the analysis
     * @return List<LossReadDto> containing all losses found for the provided analysis id
     */
    List<LossReadDto> findByAnalysisId(UUID id);

    /**
     * Create a new record of the analysis
     *
     * @param lossInsertDto The information of the new loss
     * @return LossReadDto containing the information of the created loss
     */
    LossReadDto insert(LossInsertDto lossInsertDto);

    /**
     * Updates the name of a provided loss
     *
     * @param id The id of the loss
     * @param lossUpdateDto The information containing the values to be modified for the given loss
     */
    void update(UUID id, LossUpdateDto lossUpdateDto);

    /**
     * Delete a given loss by id and the hazards associated with it.
     *
     * @param id The id of the loss
     */
    void delete(UUID id);
}
