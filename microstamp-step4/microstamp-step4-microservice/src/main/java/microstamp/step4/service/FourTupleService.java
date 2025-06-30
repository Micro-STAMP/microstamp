package microstamp.step4.service;

import microstamp.step4.dto.fourtuple.FourTupleInsertDto;
import microstamp.step4.dto.fourtuple.FourTupleReadDto;
import microstamp.step4.dto.fourtuple.FourTupleUpdateDto;

import java.util.List;
import java.util.UUID;

public interface FourTupleService {

    /**
     * Find all 4-tuples
     *
     * @return List<FourTupleReadDto> containing all 4-tuples in the database
     */
    List<FourTupleReadDto> findAll();

    /**
     * Find a 4-tuple by it UUID
     *
     * @param id The UUID from 4-tuple
     * @return FourTupleReadDto containing the information of the 4-tuple
     */
    FourTupleReadDto findById(UUID id);

    /**
     * Find all 4-tuples from an analysis
     * @param id The id of the analysis
     * @return List<FourTupleReadDto> containing all 4-tuples found for the provided analysis id
     */
    List<FourTupleReadDto> findByAnalysisId(UUID id);

    /**
     * Create a new record of 4-tuple
     *
     * @param fourTupleInsertDto The information of the new 4-tuple
     * @return FourTupleReadDto containing the information of the created 4-tuple
     */
    FourTupleReadDto insert(FourTupleInsertDto fourTupleInsertDto);

    /**
     * Updates a 4-tuple
     *
     * @param id The id of the 4-tuple
     * @param fourTupleUpdateDto The information containing the values to be modified for the given 4-tuple
     */
    void update(UUID id, FourTupleUpdateDto fourTupleUpdateDto);

    /**
     * Delete a given 4-tuple by id.
     *
     * @param id The id of the 4-tuple
     */
    void delete(UUID id);

}
