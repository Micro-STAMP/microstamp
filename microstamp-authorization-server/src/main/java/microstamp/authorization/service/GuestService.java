package microstamp.authorization.service;

import microstamp.authorization.dto.AnalysisReadDto;
import microstamp.authorization.dto.ExportReadDto;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface GuestService {

    List<AnalysisReadDto> findAll();

    AnalysisReadDto findById(UUID analysisId);

    List<ExportReadDto> exportAll();

    ExportReadDto exportToJson(UUID analysisId);

    byte[] exportToPdf(UUID analysisId) throws IOException;
}