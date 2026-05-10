package microstamp.cast.step1.service;

import microstamp.cast.step1.dto.accidentlossevent.AccidentLossEventInsertDto;
import microstamp.cast.step1.dto.accidentlossevent.AccidentLossEventReadDto;
import microstamp.cast.step1.dto.accidentlossevent.AccidentLossEventUpdateDto;

import java.util.List;
import java.util.UUID;

public interface AccidentLossEventService {

    List<AccidentLossEventReadDto> findAll();

    AccidentLossEventReadDto findById(UUID id);

    List<AccidentLossEventReadDto> findByAnalysisId(UUID id);

    AccidentLossEventReadDto insert(AccidentLossEventInsertDto accidentLossEventInsertDto);

    void update(UUID id, AccidentLossEventUpdateDto accidentLossEventUpdateDto);

    void delete(UUID id);
}