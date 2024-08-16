package microstamp.authorization.service;

import microstamp.authorization.dto.AnalysisInsertDto;
import microstamp.authorization.dto.AnalysisReadDto;
import microstamp.authorization.dto.AnalysisUpdateDto;

import java.util.List;
import java.util.UUID;

public interface AnalysisService {

    List<AnalysisReadDto> findAll();

    AnalysisReadDto findById(UUID id);

    List<AnalysisReadDto> findByUserId(UUID id);

    AnalysisReadDto insert(AnalysisInsertDto analysisInsertDto);

    void update(UUID id, AnalysisUpdateDto analysisUpdateDto);

    void delete(UUID id);
}
