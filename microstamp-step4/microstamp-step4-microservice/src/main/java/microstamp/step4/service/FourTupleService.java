package microstamp.step4.service;

import microstamp.step4.dto.fourtuple.FourTupleInsertDto;
import microstamp.step4.dto.fourtuple.FourTupleFullReadDto;
import microstamp.step4.dto.fourtuple.FourTupleUpdateDto;
import microstamp.step4.dto.unsafecontrolaction.UnsafeControlActionFullReadDto;

import java.util.List;
import java.util.UUID;

public interface FourTupleService {

    /**
     * Find all 4-tuples
     *
     * @return List<FourTupleReadDto> containing all 4-tuples in the database
     */
    List<FourTupleFullReadDto> findAll();

    /**
     * Find a 4-tuple by it UUID
     *
     * @param id The UUID from 4-tuple
     * @return FourTupleReadDto containing the information of the 4-tuple
     */
    FourTupleFullReadDto findById(UUID id);

    /**
     * Find all 4-tuples from an analysis
     * @param id The id of the analysis
     * @return List<FourTupleReadDto> containing all 4-tuples found for the provided analysis id
     */
    List<FourTupleFullReadDto> findByAnalysisId(UUID id);

    /**
     * Find all UCAs and 4-tuples from an analysis
     * @param id The id of the analysis
     * @return List<UnsafeControlActionFullReadDto> containing all UCAs and all 4-tuples found for the provided analysis id
     */
    List<UnsafeControlActionFullReadDto> findByAnalysisIdSortedByUnsafeControlActions(UUID id);

    /**
     * Find all 4-tuples that contain a given UCA
     *
     * @param ucaId The id of the UCA
     * @return List<FourTupleReadDto> containing all 4-tuples that contain the provided UCA
     */
    UnsafeControlActionFullReadDto findByUcaId(UUID ucaId);

    /**
     * Create a new record of 4-tuple
     *
     * @param fourTupleInsertDto The information of the new 4-tuple
     * @return FourTupleReadDto containing the information of the created 4-tuple
     */
    FourTupleFullReadDto insert(FourTupleInsertDto fourTupleInsertDto);

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
